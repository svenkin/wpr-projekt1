package tools;

public class SqlBooleanHelper {
	public static boolean checkIfCorrect(int numb) {
		return numb > 0;
	}
	public static int booleanToInt(boolean bool){
		return bool ? 1 : 0;
	}
}
