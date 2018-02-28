package com.example.game.domain.mapper;

import org.apache.ibatis.annotations.Update;

public interface TestMapper {

	Integer queryCount();
	
	@Update("update admin set permission = permission - 1 where a_name = 'pa_admin2' and permission!=0")
	Integer updateAdminByName();

}
