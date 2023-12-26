import java.util.List;
import java.util.Stack;
/**
 * 입력 유효성 검사기
 * - 알파벳 유효성 검사
 * - 정규식 유효성 검사
 * @author leejiwon
 *
 */
public class InputValidator {
	
	/**
	 * 알파벳 유효성 검사 
	 * - 알파벳에 띄어쓰기를 하지 않은 경우(알파벳이 여러개인 경우를 허용하지 않음. ), 
	 * - 피연산자에 특수문자가 있는 경우를 허락하지 않음.
	 * 
	 * return
	 * - valid한 input인 경우 "valid" 출력
	 * - invalid한 input인 경우 error message 출력
	 */
	public static String getAlphabetSetStrMsg(String alphabetSet) {
		
		//알파벳에 띄어쓰기를 하지 않은 경우, 알파벳 길이가 1 이상인 경우
		for(String t : alphabetSet.split(" ")) {
			if(t.length()>1) return "\n알파벳 입력 시 띄어쓰기가 필요합니다. 각 알파벳은 길이가 1.(error with \'"+t+"\')";
			if(t.contains(String.valueOf(Type.UNION.getText()))
					||t.contains(String.valueOf(Type.CONCAT.getText()))
					||t.contains(String.valueOf(Type.KLEENE.getText()))) 
				return "\n연산자는 알파벳으로 사용할 수 없습니다. (error with \'"+t+"\')";
			if(t.contains("(")||t.contains(")")) 
				return "\n괄호는 알파벳으로 사용할 수 없습니다. (error with \'"+t+"\')";
		}
		
		return "valid";
	}
	
	
	
	
	/**
	 * 정규식 유효성 검사 
	 * - 알파벳이 아닌 경우
	 * - 연산자의 위치가 부적절한 경우(빈 괄호의 경우도 연산자의 위치가 부적절한 경우로 포함함. )
	 */
	public static String getRegexStrMsg(String regex, List<Character> alphabetSet) {
		String errMsg="\n정규식을 다시 입력하세요. ";
		char[] cArray = regex.toCharArray();
		Stack<Character> brackets = new Stack<>();
		//TODO
		for(int i=0 ; i<cArray.length ; i++) {
			Character cf = (i==0) ? null : cArray[i-1];
			Character c = cArray[i];
			Character cn = (i==cArray.length-1) ? null : cArray[i+1];
			
			switch(c) {
				case '(':
					brackets.push(c);
					if(i!=cArray.length-1&&cn.equals(')')) return errMsg + "error with \'" + c + cn +"\'";
					break;
				case ')':
					if(!brackets.isEmpty()&&brackets.peek().equals('(')) brackets.pop();
					else return errMsg + "error with \'" + c +"\'";
					break;
				case '+':
					if(i==0||i==cArray.length-1) return errMsg + "error with" + c;
					else if(cf.equals('+')||cn.equals('+')||cf.equals('(')||cn.equals(')')) {
						return errMsg + "error with \'" + c +"\'";
					}
					break;
				case '*':
					if(i==0) return errMsg + "error with \'" + c +"\'";
					else if(cf.equals('+')||cf.equals('(')||cn.equals('*')) {
						return errMsg + "error with \'" + c +"\'";
					}
					break;
				default:
					if(!alphabetSet.contains(c)) return errMsg + "error with \'" + c +"\'";
					break;
			}	
		}
		
		if(!brackets.isEmpty()) {
			return errMsg + "error with \'(\'";
		}

		return "valid";
	}
	
}
