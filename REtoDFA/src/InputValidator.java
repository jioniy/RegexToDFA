import java.util.List;
import java.util.Stack;

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
			if(t.length()>1) return "알파벳 입력 시 띄어쓰기가 필요합니다. 각 알파벳은 길이가 1.(error with '"+t+"')";
			if(t.contains(String.valueOf(Type.UNION.getText()))
					||t.contains(String.valueOf(Type.CONCAT.getText()))
					||t.contains(String.valueOf(Type.CONCAT.getText()))) 
				return "연산자는 알파벳으로 사용할 수 없습니다. (error with "+t+")";
		}
		
		return "valid";
	}
	
	
	
	
	/**
	 * 정규식 유효성 검사 
	 * - 알파벳이 아닌 경우
	 * - 연산자의 위치가 부적절한 경우
	 */
	public static String getRegexStrMsg(String regex, List<Character> alphabetSet) {
		String errMsg="정규식을 다시 입력하세요.";
		Stack<Character> operatorStack = new Stack<>();
		Stack<Character> operandStack = new Stack<>();
		//TODO
		for(Character c : regex.toCharArray()) {
			switch(c) {
				case '(':
					operandStack.push(c);
					break;
				case ')':
					if(operandStack.isEmpty()||!operatorStack.isEmpty()) return errMsg+"error with '" + c + "'";
					while(true) {
						if(operandStack.peek()=='(') {
							operandStack.pop();
							break;
						}
						if(operandStack.isEmpty()){
							return errMsg+"error with '" + c + "'";
						}
					}
					break;
				case '+':
					if(operandStack.isEmpty()) return errMsg+"error with '" + c + "'";
					break;
				case '*':
					if((!operatorStack.peek().equals('(')&&operandStack.isEmpty())||operandStack.isEmpty()) return errMsg+"error with '" + c + "'";
					operandStack.clear();
					operandStack.push('C');
					break;
				default:
					break;
			}
		}
		
		if(!operandStack.isEmpty()) {
			return errMsg+"error with '" + operandStack.peek() + "'";
		}
		
		return "valid";
	}
	
}
