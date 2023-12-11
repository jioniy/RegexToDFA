import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

/**
 * @author leejiwon
 * 
 * NFA-ε Transition Table
 *
 */
public class NFAETransitionTable{
	private List<Character> symbols;
	private String[][] data;
	private NFAEState[] NFAEStateList;
	private Map<Integer, Set<NFAEState>> epsilonClosureSet;
	
	
	public NFAETransitionTable(List<Character> symbols) {
		this.symbols = new ArrayList<>();
		for(Character s : symbols) {
			this.symbols.add(s);
		}
		this.symbols.add('ε');
		data = new String[NFAEState.getStateCount()][this.symbols.size()];
		NFAEStateList = new NFAEState[NFAEState.getStateCount()];
		epsilonClosureSet = new HashMap<>();
	}
	
	public void set(NFAEState state){
		NFAEStateList[state.getID()] = state;
		
		for(Map.Entry<Character, List<NFAEState>> entry : state.getNextStates().entrySet()) {
			Character symbol = entry.getKey();
			List<NFAEState> nextStates = entry.getValue();
			
			String nextStateStr = "{";
			
			for(NFAEState ns : nextStates) {
				nextStateStr += ("q"+Integer.toString(ns.getID())+",");
			}	
			nextStateStr += "}";
			
			data[state.getID()][symbols.indexOf(symbol)] = nextStateStr;
			
			for(NFAEState ns : nextStates) {
				if(!(String.join("",data[ns.getID()]).contains("{")&&String.join("",data[ns.getID()]).contains("}"))) {
					set(ns);
				}
			}
		}
		
	}
	
	public void print() {
		System.out.print("     ");
		//print symbols
		for(int i = 0; i<symbols.size();i++) {
			System.out.print("     "+symbols.get(i)+"      ");
		}
		
		System.out.println();
		// System.out.println("-".repeat(symbols.size()*12+7));
		for(int i =0 ;i<symbols.size()*12+7;i++) {
			System.out.print("-");
		}
		System.out.println();
		
		
		for(int i = 0; i<data.length;i++) {
			System.out.print("q"+i+" : ");
			for(int j = 0; j<data[i].length; j++) {
				String nextStr = (data[i][j] != null)? data[i][j] : "---";
				System.out.print("    "+nextStr+"    ");
			}
			System.out.println();
		}
	}
	
	// epsilon closure 구하기 - 자기 자신 + epsilon으로 이동 가능한 모든 위치 탐색
	public void setEpsilonClosure() {
		for(int i=0;i<NFAEStateList.length;i++) {
			Set<NFAEState> epsilonClosureListOfState = new HashSet<>();
			findEpsilonClosureOfState(NFAEStateList[i], epsilonClosureListOfState);
			epsilonClosureSet.put(NFAEStateList[i].getID(), epsilonClosureListOfState);
		}
	}
	
	
	public void findEpsilonClosureOfState(NFAEState state, Set<NFAEState> epsilonClosureListOfState) {
		epsilonClosureListOfState.add(state);
		if(!state.getNextStates().containsKey('ε')) return;
		for(NFAEState s : state.getNextStates().get('ε')) {
			epsilonClosureListOfState.add(s);
			findEpsilonClosureOfState(s,epsilonClosureListOfState);
		}
	}
	
	
	public void printEpsilonClosure() {
		for(Map.Entry<Integer, Set<NFAEState>> entry : epsilonClosureSet.entrySet()) {
			Integer stateId = entry.getKey();
			Set<NFAEState> states = entry.getValue();
			
			System.out.print("ε-closure(q"+stateId+") = ");
			
			String res = "{";
			
			for(NFAEState s : states) {
				res += ("q"+Integer.toString(s.getID())+",");
			}	
			res += "}";
			
			System.out.println(res);
		}
	}
	
	public Map<Integer, Set<NFAEState>> getEpsilonClosureSet(){
		return epsilonClosureSet;
	}
}
