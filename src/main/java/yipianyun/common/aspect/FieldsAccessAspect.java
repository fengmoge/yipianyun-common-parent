package yipianyun.common.aspect;
 
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import yipianyun.common.core.SysConstants;
import yipianyun.common.utils.FieldAccessUtil;

@Aspect
@Component
public class FieldsAccessAspect {

	/**
	 * 新增
	 * 插入操作实体类中的创建人，创建时间赋值
	 * @param JoinPoint
	 * @throws Exception
	 */
	@Before("execution(* seentao..*.service.impl.*Impl.save*(..))")
	public void beforeAdviceToSave(JoinPoint JoinPoint) throws Exception {
		Object[] entity = JoinPoint.getArgs();
		for (Object object : entity) {
			if (object instanceof List) {
				List<?> list = (List<?>) object;
				if (list != null && !list.isEmpty()) {
					FieldAccessUtil.setFieldValueList(list, SysConstants.INSERT);
				}
			} else {
				FieldAccessUtil.setFieldValue(object, SysConstants.INSERT);
			}
		}
	}

	/**
	 * 修改
	 * 操作实体类中的修改人，修改时间赋值
	 * @param JoinPoint
	 * @throws Exception
	 */
	@Before("execution(* seentao..*.service.impl.*Impl.modify*(..))")
	public void beforeAdviceToUpdate(JoinPoint JoinPoint) throws Exception {
		Object[] entity = JoinPoint.getArgs();
		for (Object object : entity) {
			if (object instanceof List) {
				List<?> list = (List<?>) object;
				if (!list.isEmpty()) {
					FieldAccessUtil.setFieldValueList(list, SysConstants.UPDATE);
				}
			} else {
				FieldAccessUtil.setFieldValue(object, SysConstants.UPDATE);
			}
		}
	}
}
