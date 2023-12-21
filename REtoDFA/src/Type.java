import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JiwonLee
 *
 *Expression 타입
 * SYMBOL - alphabet
 * CONCAT
 * UNION
 * KLEENE
 */
enum Type{
	SYMBOL(' ', 0),
	UNION('+', 1),
	CONCAT('.', 2),
	KLEENE('*', 3); // priority 값이 높을 수록 우선임. 
	
	private final Character text;
	private final int priority;
	
	Type(Character text, int priority){
		this.text = text;
		this.priority = priority;
	}
	
	public Character getText() {
		return text;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public static Type findTypeByText(Character text) {
		return Arrays.stream(values())
				.filter(type -> type.text == text)
				.findAny()
				.orElse(null);
		
	}
	public static boolean isHigherPriority(char a, char b) {
        int ap = findTypeByText(a).priority;//a 우선순위
        int bp = findTypeByText(b).priority;//b 우선순위
        
        return ap > bp;
    }
	
	public static List<Character> getOperationList(){
		return Arrays.stream(values()).map(e->e.text).collect(Collectors.toList());
	}
}
