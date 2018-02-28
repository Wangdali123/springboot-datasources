主要根据填充Spring Boot + Mybatis多数据源和动态数据源配置 文章中动态部分坑

应用场景
    读写分离，多数据源，主从库

具体实现
1、配置文件application.yml
##多数据源
datasource:
  #主库
  master:
    jdbcUrl: jdbc:mysql://localhost:3306/pa_yqs_game?useUnicode=true&characterEncoding=utf-8
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
  #从库
  slave:
    #并非url而是jdbcUrl(因为这个在获取数据源时一直报错，看了DataSource的属性才知道是jdbcUrl)
    jdbcUrl: jdbc:mysql://localhost:3306/data_count?useUnicode=true&characterEncoding=utf-8
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
      
##mybatis
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.*.domain.pojo
  check-config-location: true
  config-location: classpath:mybatis-config.xml
2、Application启动类
/**
 * **在最外侧,即包含所有子包 
 * @ClassName Application
 * @Description 入口,启动类
 * @author lide
 * @date 2018年2月9日 下午2:47:04
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@EnableTransactionManagement(order = 2)	//设置事务执行顺序(需要在切换数据源之后，否则只走默认库)
@MapperScan(basePackages = "com.example.*.domain.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
    1.  需要设置事务(本质也是AOP)执行的顺序，否则事务的执行顺序高于后续的AOP，会导致动态切换数据源失效
   2. exclude = {DataSourceAutoConfiguration.class}用于禁用掉默认的数据源获取方式，默认会读取配置文件的据源（spring.datasource.*）
    3. MapperScan()指向的是mapper
3、代码实现
3.1 DataSourceType数据源枚举
public enum DataSourceType {

	// 主表
	Master("master"),
	// 从表
	Slave("slave");

	private String name;

	private DataSourceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
3.2 数据源上下文JdbcContextHolder(ThreadLocal)
public class JdbcContextHolder {
	
	private final static ThreadLocal<String> local = new ThreadLocal<>();
	
	public static void putDataSource(String name) {
		local.set(name);
	}
	
	public static String getDataSource() {
		return local.get();
	}
	
}
3.3 AbstractRoutingDataSource实现类DynamicDataSource(关键)
    AbstractRoutingDataSource抽象类知识，实现AOP动态切换的关键
    1.AbstractRoutingDataSource中determineTargetDataSource()方法中获取数据源 
		Object lookupKey = determineCurrentLookupKey();
		DataSource dataSource = this.resolvedDataSources.get(lookupKey);
		根据determineCurrentLookupKey()得到Datasource,并且此方法是抽象方法,应用可以实现
    2.resolvedDataSources的值根据targetDataSources所得
			afterPropertiesSet()方法中(在@Bean所在方法执行完成后，会调用此方法)：
				Map.Entry<Object, Object> entry : this.targetDataSources.entrySet()
    3.然后在xml中使用<bean>或者代码中@Bean 设置DynamicDataSource的defaultTargetDataSource(默认数据源)和targetDataSources(多数据源)
    4.利用自定义注解，AOP拦截动态的设置ThreadLocal的值
    5.在DAO层与数据库建立连接时会根据ThreadLocal的key得到数据源
		代码：getConnection()
				determineTargetDataSource().getConnection();(determineTargetDataSource返回的是DataSource)
public class DynamicDataSource extends AbstractRoutingDataSource {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected Object determineCurrentLookupKey() {
		logger.info("数据源为{}",JdbcContextHolder.getDataSource());
		return JdbcContextHolder.getDataSource();
	}
	
}
3.4 AOP切换数据源 DataSourceAspect
@Aspect
@Order(1)	//设置AOP执行顺序(需要在事务之前，否则事务只发生在默认库中)
@Component
public class DataSourceAspect {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//切点
	@Pointcut("execution(* com.example.*.service..*.*(..)))")
	public void aspect() { }
	
	@Before("aspect()")
	private void before(JoinPoint point) {
		Object target = point.getTarget();  
        String method = point.getSignature().getName();  
        Class<?> classz = target.getClass();  
        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())  
                .getMethod().getParameterTypes();  
        try {  
            Method m = classz.getMethod(method, parameterTypes);  
            if (m != null && m.isAnnotationPresent(MyDataSource.class)) {  
            	MyDataSource data = m.getAnnotation(MyDataSource.class);  
                JdbcContextHolder.putDataSource(data.value().getName()); 
                logger.info("===============上下文赋值完成:{}",data.value().getName());
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        
	}
}
3.5 自定义注解MyDataSource
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD) 
public @interface MyDataSource {

	DataSourceType value() default DataSourceType.Master;	//默认主表
	
}
3.6 数据源配置
注意@Primary标注多数据源，否则会产生实现类冲突。

@Primary和@Qualifier这两个注解的意思：　　

@Primary：  意思是在众多相同的bean中，优先使用用@Primary注解的bean.
@Qualifier ： 这个注解则指定某个bean有没有资格进行注入。
@Configuration
public class DataSourceConfig {

	@Bean(name = "master")
	@ConfigurationProperties(prefix = "datasource.master") 
	public DataSource dataSource1() {
		System.out.println("主配");
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "slave")
	@ConfigurationProperties(prefix = "datasource.slave") 
	public DataSource dataSource2() {
		System.out.println("从配");
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="dynamicDataSource")
	@Primary	//优先使用，多数据源
	public DataSource dataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		DataSource master = dataSource1();
		DataSource slave = dataSource2();
		//设置默认数据源
		dynamicDataSource.setDefaultTargetDataSource(master);	
		//配置多数据源
		Map<Object,Object> map = new HashMap<>();
		map.put(DataSourceType.Master.getName(), master);	//key需要跟ThreadLocal中的值对应
		map.put(DataSourceType.Slave.getName(), slave);
		dynamicDataSource.setTargetDataSources(map);			
		return dynamicDataSource;
	}
	
}
