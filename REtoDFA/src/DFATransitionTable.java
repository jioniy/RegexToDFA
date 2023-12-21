import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author JiwonLee
 * 
 * set & print DFA Transition Table
 *
 */
public class DFATransitionTable {
	private List<Character> symbols;
	private String data[][];
	
	public DFATransitionTable(List<Character> symbols) {
		this.symbols = new ArrayList<>();
		for(Character s : symbols) {
			this.symbols.add(s);
		}
		data = new String[DFAState.getStateCount()][this.symbols.size()];
	}
	
	public void set(DFAState state) {
		for(Map.Entry<Character, DFAState> entry : state.getNextStates().entrySet()) {
			Character symbol = entry.getKey();
			DFAState ns = entry.getValue();
			
			data[state.getID()][symbols.indexOf(symbol)] = "{Q"+Integer.toString(ns.getID())+"}";
			
			if(!(String.join("",data[ns.getID()]).contains("{")&&String.join("",data[ns.getID()]).contains("}"))) {
				set(ns);
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
			System.out.print("Q"+i+" : ");
			for(int j = 0; j<data[i].length; j++) {
				String nextStr = (data[i][j] != null)? data[i][j] : "---";
				System.out.print("    "+nextStr+"    ");
			}
			System.out.println();
		}
	}
}
