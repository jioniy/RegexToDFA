/**
 * is a string s in language?
 * 
 * @author jioniy
 *
 */
public class DeterministicAccepter {
	DFAState DFA;
	DFAState startDFA;
	NFAEState finalState;
	
	public DeterministicAccepter(DFAState DFA, NFAEState finalState){
		this.DFA = DFA;
		this.startDFA = DFA;
		this.finalState = finalState;
	}
	
	public boolean isAccepted(String inputStr) {
		
		DFA = startDFA;//DFA state 위치 초기화
		
		int i=0;
		while(i<inputStr.length()) {
			char c = inputStr.charAt(i);
			if(DFA.getNextStates().get(c)==null) {
				return false;
			}
			DFA = DFA.getNextStates().get(c);
			i++;
		}
		if(DFA.getNFAStateSet().contains(finalState)) return true;
		else {
			return false;
		}
	}
}
