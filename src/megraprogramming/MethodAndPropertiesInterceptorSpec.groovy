package megraprogramming

import spock.lang.Specification

class MethodAndPropertiesInterceptorSpec extends Specification {

	def "should intercept all method and property access"() {
		given:
		MethodAndPropertiesInterceptor imap = new MethodAndPropertiesInterceptor()
		
		when:
		def result = imap.text
		
		then:
		result == "1"
		imap.@calls == ["text"]
		
		when:
		imap.text = "2"
		result = imap.text
		
		then:
		result == "2"
		imap.@calls == ["text", "text=2", "text"]
		
		when:
		result = imap.existingMethod("the arg")
		
		then:
		result == "the arg"
		imap.@calls == ["text", "text=2", "text", "intercepting existingMethod([the arg])", "existingMethod(the arg)"]
	}
	
	class MethodAndPropertiesInterceptor implements GroovyInterceptable {
		
		List<String> calls = []
		String text = "1"
		
		String existingMethod(String arg) {
			calls << "existingMethod($arg)"
			return arg
		}
		
		public Object getProperty(String name) {
			calls << name
			this.@"$name"
		}
		
		public void setProperty(String name, Object value) {
			calls << "$name=$value"
			this.@"$name" = value
		}
		
		public Object invokeMethod(String name, Object args) {
			calls << "intercepting $name($args)"
			metaClass.getMetaMethod(name, args)?.invoke(this, args)
		}
	}
}
