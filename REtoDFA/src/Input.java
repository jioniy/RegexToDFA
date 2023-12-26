import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 사용자 input 처리
 * 1. alphabet 입력
 * 2. regular expression 입력
 * 
 * @author jiwon Lee
 *
 */
public class Input {

	/**
	 * input Alphabets 입력
	 * @return
	 */
	public static List<Character> inputStringToAlphabetSet(Scanner sc) {
		String s;
		String msg;
		
		while(true) {
			System.out.println("[1] alphabet을 입력하세요.(ex) A b c 입력 시 => {'A', 'b', 'c'}");
		
			s = sc.nextLine();
			msg = InputValidator.getAlphabetSetStrMsg(s);
			
			if(msg.equals("valid")) break;
			else System.out.println(msg);
		
		}
		
		List<Character> alphabetSet = new ArrayList<>();
		for(String t : s.split(" ")) {
			char c = t.charAt(0);
			if(alphabetSet.contains(c)) continue;
			alphabetSet.add(c);
		}
		
		return alphabetSet;
	}
	
	
	/**
	 * input Regex String 입력
	 * @return
	 */
	public static String inputRegexString(Scanner sc, List<Character> alphabetSet) {
		String regex;
		String msg;
		
		while(true) {
			System.out.println("\n[2] Regular Expression을 입력하세요.");
			regex = sc.nextLine();
			msg = InputValidator.getRegexStrMsg(regex, alphabetSet);
			if(msg.equals("valid")) break;
			else System.out.println(msg);
		}
		
		return regex;
	}
}
