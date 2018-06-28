package cn.lucasma.activiti.mapper;

import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author Lucas Ma
 */
public interface MyCustomMapper {

    @Select("SELECT * FROM ACT_RU_TASK")
    public List<Map<String, Object>> findAll();
}
