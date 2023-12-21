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
	public static List<Character> inputStringToAlphabetSet() {
		Scanner sc = new Scanner(System.in);
		String s;
		String msg;
		
		while(true) {
			System.out.println("alphabet을 입력하세요.(ex) A b c 입력 시 => {'A', 'b', 'c'}");
		
			s = sc.nextLine();
			msg = InputValidator.getAlphabetSetStrMsg(s);
			
			if(msg.equals("valid")) break;
			else System.out.println(msg);
		
		}
		sc.close();
		
		
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
	public static String inputRegexString(List<Character> alphabetSet) {
		System.out.println("Regular Expression을 입력하세요.");
		Scanner sc = new Scanner(System.in);
		String regex;
		
		while(true) {
			regex = sc.nextLine();
			if(true) {
				break;
			}
		}
		
		
		sc.close();
		return regex;
	}
}
