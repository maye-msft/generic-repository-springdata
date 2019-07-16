package com.github.mayemsft.springdata.genericrepository;

import org.mdkt.compiler.InMemoryJavaCompiler;

class CompilerFactory {

	
	private CompilerFactory() {
		
	}
	
	private static CompilerFactory factory;
	
	private static ClassLoader classLoader;
	
	private static InMemoryJavaCompiler compiler;
	
	public static CompilerFactory getInstance() {
		if(factory == null) {
			factory = new CompilerFactory();
			classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();
			compiler= InMemoryJavaCompiler.newInstance();
			compiler.useParentClassLoader(classLoader);
		}
		return factory;
	}
	
	protected Class<?> complie(String className, String code) throws Exception {
		
		return compiler.compile(className, code);
		
	} 
	
	protected ClassLoader getClassLoader() {
		
		return compiler.getClassloader();
		
	} 
}
