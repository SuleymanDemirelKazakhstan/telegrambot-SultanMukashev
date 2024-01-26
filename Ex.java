import java.util.*;
public class Ex{
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        String num = s.nextLine();
        String str = s.nextLine();
        String[] arr = num.split(" ");
        String[] arr1 = str.split(" ");
        String ans = "";
        HashMap<Character, Character> map = new HashMap<Character, Character>(); 
        for(int i = 0; i < arr1[0].length(); i++){
            map.put(str.charAt(i), num.charAt(i));
        }
        for(int i = 0; i < arr1[1].length(); i++){
            ans+=map.get(arr1[1].charAt(i));
        }
        System.out.println(arr[0]+" "+ans);
    }
}
