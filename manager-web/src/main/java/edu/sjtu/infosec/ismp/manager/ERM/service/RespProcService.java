package edu.sjtu.infosec.ismp.manager.ERM.service;

import java.util.List;

import edu.sjtu.infosec.ismp.manager.ERM.model.ContiRespProc;
import edu.sjtu.infosec.ismp.manager.ERM.model.RespInfoBO;
import edu.sjtu.infosec.ismp.manager.ERM.model.SafeThreatenInfo;

public interface RespProcService {
	//查询威胁安全列表
	List<SafeThreatenInfo> findSafeThreat();
	
	//通过预案编号查询应急响应过程列表
	List<ContiRespProc> findRespProcByrespinfo(int pageNow,int pageSize,String id,String sid);
	
	//根据id查询RespInfoBO对象
	 public RespInfoBO findRespBoById(String id);
	 
	//根据id查询SafeThreatenInfo对象
	 SafeThreatenInfo findSafeThreatenInfoById(String id);
	 
	//根据RespInfoBO id,SafeThreatenInfo id 查询对应的应急响应响应过程列表
	 List<ContiRespProc>  findallByTwoId(RespInfoBO rid ,SafeThreatenInfo sid);
	 
	 //根据威胁id查询威胁名称
	 String findSafeNameById(String id);
	 //删除
	 void delete(ContiRespProc con);
	 
	 //更新
	 void update(ContiRespProc con);
	 
	 //添加
	 void save(ContiRespProc con);
	 
	 void deleteProcByRespInfo(RespInfoBO resp);
	 
	 //获得总条数
	 int getCount(String respid,String safeid);
}
