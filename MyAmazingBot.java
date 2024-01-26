import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class MyAmazingBot extends TelegramLongPollingBot {
    static DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
    static HashMap<Long, HashMap<LocalDateTime, String>> usersbookings = new HashMap<Long, HashMap<LocalDateTime, String>>();
    static HashMap<LocalDateTime, String> empty = new HashMap<LocalDateTime, String>();
    @Override
    
    public void onUpdateReceived(Update update) {
        long chat_id = update.getMessage().getChatId();
        usersbookings.put(chat_id, new HashMap<LocalDateTime, String>());
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
                if(message_text.equals("/start")){
                    

                SendMessage ans = new SendMessage(); // Create a message object object
                ans.setChatId(chat_id);
                ans.setText(String.valueOf("Hello!\nWelcome too SDU booking bot!\nSend /commands to see what can I do!"));
                try {
                    execute(ans); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                }
                else if(message_text.equals("/commands")){
                    
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText(String.valueOf("/booking + room + date inf format(yyyy.MM.dd HH:mm-HH:mm).\n/list - to get a list of booked rooms(others are available).\n/Mylist - to get rooms that only you had booked.\n/cancel + room + date in format(yyyy.MM.dd HH:mm-HH:mm)."));
                    try {
                    execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            
            else if(message_text.contains("/booking")){
                // String[] times = message_text.split("");
                String[] mess = message_text.split(" ");
                String name = mess[1].toUpperCase();
                String date = mess[2];
                String[] interval = mess[3].split("-");
                LocalDateTime time1 = LocalDateTime.parse(date+" "+interval[0], form);
                LocalDateTime time2 = LocalDateTime.parse(date+" "+interval[1], form);
                
                if(!Room.isWeekEnd(time1)){
                    if(name.matches("[ABCDEFGH][1-3][0-1][1-8]") || name.matches("[ABCD][1-2]") || name.matches("[H][0][1-3]}") ){
                    if(time1.isAfter(LocalDateTime.parse(date+" 09:00", form)) && time2.isBefore(LocalDateTime.parse(date+" 18:00", form))){
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText(Room.booking(time1, time2, name));
                    
                    for(LocalDateTime t = time1; t.isBefore(time2); t = t.plusHours(1)){
                       { usersbookings.get(chat_id).put(t, name);
                    }
                    }  
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("You can book a room only between 9:00-18:00!");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else{
                SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("Choose an existing room!");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
        }else{
            SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("It is a weekend!");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        }
    }
        
            else if(message_text.contains("/list")){
                    SendMessage ans = new SendMessage(); // Create a message object object
                    
                    ans.setChatId(chat_id);
                    ans.setText(Room.list());
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

            }
            else if(message_text.contains("/mylist")){
                SendMessage ans = new SendMessage(); // Create a message object object
                
                ans.setChatId(chat_id);
                ans.setText(Mylist(chat_id));
                try {
                    execute(ans); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

        } 
        else if(message_text.contains("/cancel")){
            // String[] times = message_text.split(" at ");
            String[] mess = message_text.split(" ");
            String name = mess[1];
            String date = mess[2];
            String[] interval = mess[3].split("-");
            LocalDateTime time1 = LocalDateTime.parse(date+" "+interval[0], form);
            LocalDateTime time2 = LocalDateTime.parse(date+" "+interval[1], form);
            SendMessage ans = new SendMessage(); // Create a message object object
                    
                    ans.setChatId(chat_id);
                    ans.setText(cancel(chat_id, time1, time2, name));
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

        } 
            else{
            System.out.println(message_text + "😀");
            

            SendMessage message = new SendMessage(); // Create a message object object
            message.setChatId(chat_id);
            message.setText(message_text + "😀");
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        }
    }
   public static String Mylist(Long id){
    String ans = "You booked:\n";
    for(LocalDateTime time: usersbookings.get(id).keySet()){
            ans+="Room "+usersbookings.get(id).get(time)+" at "+time.format(form)+"\n";
        }
    
    return ans;
   }
   public static String cancel(Long id, LocalDateTime time1, LocalDateTime time2, String name){
    for(LocalDateTime t = time1; t.isBefore(time2); t = t.plusHours(1)){
        usersbookings.get(id).remove(t);
        Room.reserved.get(t).remove(name);
        
    }
    return "Your booking is cancelled";
   }
    @Override
    public String getBotUsername() {
        
        return "SDU_bookingbot";
    }

    @Override
    public String getBotToken() {
        return "5941034705:AAFvnZqjbR7OO5N42svRU_SLkKPTYz5Z39c";
    }
}
class Room{
    static HashMap<LocalDateTime, HashSet<String>> reserved = new HashMap<LocalDateTime, HashSet<String>>();
    String roomname;
    static DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static DateTimeFormatter formdate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
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
                reserved.put(t, new HashSet<String>());
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
                ans+=time.format(customFormat)+" "+name+" is booked\n";
            }
        }
        return ans;
    }    
    public static boolean isWeekEnd(LocalDateTime localDate)
    {
        String dayOfWeek = localDate.getDayOfWeek().toString();
        if("SATURDAY".equalsIgnoreCase(dayOfWeek)||
        "SUNDAY".equalsIgnoreCase(dayOfWeek))
        {
            return true;
        }
        return false;
    }

}