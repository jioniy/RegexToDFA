import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leejiwon
 * 
 * NFA-ех Transition Table
 *
 */
public class NFAETransitionTable{
	private String[][] data;
	
	
	public NFAETransitionTable(List<Character> symbols) {
		symbols.add('ех');
		
		data = new String[NFAEState.getStateCount()][symbols.size()];

	}
	
	public void set(List<Character> symbols, NFAEState state){
		
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
					set(symbols, ns);
				}
			}
		}
		
	}
	
	public void print(List<Character> symbols) {
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
}
