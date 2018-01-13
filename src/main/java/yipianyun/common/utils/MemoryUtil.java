package yipianyun.common.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import yipianyun.base.cache.CacheType;
import yipianyun.base.cache.Constant;
 
public class MemoryUtil {
	
	public static final ConcurrentMap<String,Collection> MAP=new ConcurrentHashMap<String,Collection>();
	public static final Set<String> SET=new HashSet<String>();
	public  static void setCollection(String key ,Collection<?> collection){
		if(collection==null){
			return ;
		}
			MAP.put(key, collection);
			SET.add(key);
			MAP.put(Constant.IHR_CACHE_PREFIX+CacheType.MEMORY.name()+"-"+CacheUtils.contentPrefix+"keys(String)", SET);
	}
	
	public static  Collection<?> getCollection(String key){
		Collection<?> col = MAP.get(key);
		if(col==null) return null;
		if(col instanceof List){
			return (List<?>)col;
		}
		if(col instanceof Set){
			return (Set<?>)col;
		}
		return col;
	}
	
	public  static void clearCache(){
			for (String str : SET) {
				MAP.put(str, null);
			}
	}
}
