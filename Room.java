import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
public class Room{
    static HashMap<LocalDateTime, HashSet<String>> reserved = new HashMap<LocalDateTime, HashSet<String>>();
    String roomname;
    static DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static DateTimeFormatter formdate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
   
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    Set<String> rooms = new HashSet<String>(Arrays.asList(""));

    Room(String name){
        this.roomname = name;
    }
    public static boolean available(LocalDateTime time, String room){
        if(reserved.containsKey(time)){
            if(reserved.get(time).contains(room)) return false;
            else return true;
        }
        else return true;
    }
    public static String booking(LocalDateTime time1, LocalDateTime time2, String room){
        boolean available = false;
        String ans = "";
        for(LocalDateTime t = time1; t.isBefore(time2); t = t.plusHours(1)){
            if(available(t, room)) available = true;
            else { available = false; break;}
        }
        if(available){
            for(LocalDateTime t = time1; t.isBefore(time2); t = t.plusHours(1)){
                reserved.get(t).add(room);
            }   
            ans = "You booked room "+room+" on "+time1.format(formdate)+" from "+time1.format(formatter)+" till "+time2.format(formatter);
        }else ans = "This room is already booked at that date";
        return ans ;
    }
    public static String toString(LocalDateTime time, String room){
        String ans = "";
        ans = room+" is booked on "+time.format(customFormat);
        return ans;
    }
    public static String list(){
        String ans = "";
        for(LocalDateTime time: reserved.keySet()){
            for(String name: reserved.get(time)){
                ans+=time.format(customFormat)+" "+name+" is booked";
            }
        }
        return ans;
    }    
    
}