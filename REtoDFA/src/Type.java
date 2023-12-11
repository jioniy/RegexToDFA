import java.util.Arrays;

/**
 * @author leejiwon
 *
 *Expression Ÿ��
 * SYMBOL - alphabet
 * CONCAT
 * UNION
 * KLEENE
 */
enum Type{
	SYMBOL(' ', 0),
	UNION('+', 1),
	CONCAT('.', 2),
	KLEENE('*', 3); // priority ���� ���� ���� �켱��. 
	
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
        int ap = findTypeByText(a).priority;//a �켱����
        int bp = findTypeByText(b).priority;//b �켱����
        
        return ap > bp;
    }
}
