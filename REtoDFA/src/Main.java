import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
/**
 * 
 * 0. Input Alphabet Set & Regular Expression
 * - Input
 * - InputValidator
 * 
 * 1. Regular Expression(String Input) to RegexTree
 * - RegexTree
 * - RegexTreeConverter
 * 
 * 2. Regular Expression To NFA-ε
 * - NFAEState
 * - NFAEConverter
 * - NFAETransitionTable
 * 
 * 3. NFA-ε to DFA
 * - DFAState
 * - DFAConverter
 * - DFATransitionTable
 * 
 * 4. String Check
 * - DeterministicAccepter
 * 
 * 
 * > TODO 
 * - 파일 구조 리팩토링
 * 
 * @author JiwonLee
 * 
 */
public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		/**
		 * 1. 사용자 입력
		 * 
		 */
		System.out.println("===================[프로그램 시작]===================");
		
		List<Character> alphabetSet = new ArrayList<>();
		
		/*
		 * 1-1. 알파벳 입력
		 * 
		 */
		alphabetSet = Input.inputStringToAlphabetSet(sc);
		
		/*
		 * 1-2. 정규식 입력
		 * 
		 */
		
		String regex = Input.inputRegexString(sc, alphabetSet);

		
		/*
		 * 2. 정규식 트리 구성 
		 * 
		 */
		RegexTree rTree = RegexTreeConverter.convert(alphabetSet, regex);//RE TREE 구성
		
		
		/*
		 * 3. 정규식에 대한 NFA-ε 구하기
		 */
		NFAEConverter nc = new NFAEConverter();
		
		Stack<NFAEState> endStateStack = new Stack<>();
		NFAEState startState = nc.convertToFiniteAutomata(rTree, endStateStack);//정규식에 대한 오토마타 변환
		NFAEState finalState = endStateStack.pop();
		
		
		/*
		 * 4. NFA-ε
		 * - Start, Final State
		 * - Transition Table
		 * 
		 */
		System.out.println("\n===================<RESULT>===================");
		System.out.println("\n===> NFA-ε");
		System.out.println("Start State : q"+startState.getID());
		System.out.println("Final State : q"+finalState.getID());
		System.out.println("Transition Table : ");
		NFAETransitionTable nt = new NFAETransitionTable(alphabetSet);
		nt.set(startState);//transition을 table 형태(2차원 배열)로 구성
		nt.print();//출력
		
		
		/**
		 * 5. NFA-ε to DFA
		 * - ε-closure 
		 * - Transition table
		 */
		System.out.println();
		System.out.println("\n===> ε-closure");
		nt.setEpsilonClosure();
		nt.printEpsilonClosure();
		
		
		System.out.println();
		System.out.println("\n===> DFA");
		DFAConverter dc = new DFAConverter();
		
		DFAState dfa = dc.convertToDFA(startState, nt.getEpsilonClosureSet());
		System.out.println("States : ");
		dc.printDFAStates();
		System.out.println("\nAccepting States : ");
		dc.printDFAAcceptingStates(finalState);
		
		System.out.println("\n\nTransition Table : ");
		DFATransitionTable dt = new DFATransitionTable(alphabetSet);
		dt.set(dfa);
		dt.print();
		
		
		
		/**
		 * 
		 * 6. 문자열 확인
		 * 문자열 입력 
		 * accept / reject
		 */
		
		System.out.println();
		System.out.println("\n===================<CHECK>===================");
		
		DeterministicAccepter da = new DeterministicAccepter(dfa, finalState);
		
		System.out.println("\n[3] 확인하고 싶은 문자열을 입력하세요. \':q\'를 입력하면 종료됩니다.");
		String inputStr = "";
		
		while(true){
			System.out.print(">");
			
			inputStr = sc.nextLine();
			if(inputStr.equals(":q")) {
				System.out.println();
				System.out.println("===================[프로그램 종료]===================");
				break;
			}
			if(da.isAccepted(inputStr)) System.out.println("Accepted!");
			else System.out.println("Rejected!");
			System.out.println();
		}
		
		
		sc.close();
	}

}
