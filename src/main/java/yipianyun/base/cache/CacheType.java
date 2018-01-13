package yipianyun.base.cache;

public enum CacheType {
 
	NONE("无缓存",0),
	SIGLE("Redis单机版缓存",1),
	CLUSTER("Redis集群版缓存",2),
	MEMORY("内存型缓存",3);
	
	private String name;
	private int index;
	private CacheType(String name,int index){
		this.name=name;
		this.index=index;
	}
	@Override
	public String toString(){
		return String .valueOf(this.index+":"+this.name);
	}
}
