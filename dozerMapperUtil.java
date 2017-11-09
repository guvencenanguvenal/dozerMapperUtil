@Component
public class DozerUtil {

	@Autowired
	private DozerBeanMapper dozerMapper;

	/**
	 * @author GuvenG
	 * 
	 * this method is mapping method which is custom converter for Collection.
	 * 
	 * @param source
	 * @param destinationClass
	 * @return
	 */
	public <T> T map(Object source, Class<T> destinationClass){

		T destObj;
		
		try {
			destObj = destinationClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
		
		destObj = dozerMapper.map(source, destinationClass);
		
		//Get class fields
		for (Field f : source.getClass().getDeclaredFields()){
			//Add range for List property
			if (f.getType().equals(List.class)){
				try {
					//Has a get method for this property?
					if (hasAvaibleGetMethod(source.getClass(), f.getName()) && hasAvaibleGetMethod(destObj.getClass(), f.getName())){
					
						List<Object> sourceList = (List<Object>)(getAvaibleGetMethod(source.getClass(), f.getName()).invoke(source));
					
						//Add range
						for (Object sourceListItem : sourceList)
							((List<Object>)(getAvaibleGetMethod(destObj.getClass(), f.getName()).invoke(destObj))).add(sourceListItem);
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return destObj;
	}
	
	/**
	 * @author GuvenG
	 * 
	 * @param targetClass
	 * @param propertyName
	 * @return
	 */
	private <T> boolean hasAvaibleGetMethod(Class<T> targetClass, String propertyName){
		String getMethodName =  "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

		try {
			targetClass.getMethod(getMethodName);
		} catch (NoSuchMethodException e) {
			return false;
		} catch (SecurityException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * @author GuvenG
	 * 
	 * @param targetClass
	 * @param propertyName
	 * @return
	 */
	private <T> Method getAvaibleGetMethod(Class<T> targetClass, String propertyName){
		String getMethodName =  "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
		Method method = null;
		
		try {
			method = targetClass.getMethod(getMethodName);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
		
		return method;
	}
	
	
}
