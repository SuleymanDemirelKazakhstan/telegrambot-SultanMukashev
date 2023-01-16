import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class MyAmazingBot extends TelegramLongPollingBot {
    ArrayList<int[]> answers = new ArrayList<int[]>();
    static boolean[] booked = new boolean[10];
    static String[] bookinglist = new String[10];  
    ArrayList<String> roomnames = new ArrayList<String>(); 
    ArrayList<boolean[][]> rooms = new ArrayList<boolean[][]>();
    String[] days = {"monday","tuesday","wednesday","thursday","friday"};
    
    @Override
    
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
                if(message_text.equals("/start")){ 
                    long chat_id = update.getMessage().getChatId();

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
                    long chat_id = update.getMessage().getChatId();
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText(String.valueOf("/booking room + index + day + period of time separated by '-' - to book a room at that time.\n/list room + index + day of the week - to get a list of rooms to know if they are booked or not.\n/addroom + roomname - to add new room to the list\n/getroomsList - to get all names of rooms with indexes\n"));
                    try {
                    execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            //     else if(message_text.substring(0,4).equals("")){
            //     String[] inputs = message_text.substring(5).split(" ");
            //     int res = Integer.valueOf(inputs[0])+Integer.valueOf(inputs[1]);
            //     int[] inp = new int[3];
            //     for(int i = 0; i < 2; i++){
            //         inp[i] = Integer.valueOf(inputs[i]);
            //     }
            //     inp[2] = res;
            //     answers.add(inp);
            //     System.out.println(String.valueOf(res));
            //     long chat_id = update.getMessage().getChatId();

            //     SendMessage ans = new SendMessage(); // Create a message object object
            //     ans.setChatId(chat_id);
            //     ans.setText(String.valueOf(res));
            //     try {
            //         execute(ans); // Sending our message object to user
            //     } catch (TelegramApiException e) {
            //         e.printStackTrace();
            //     }
            // }


            // else if(message_text.equals("/getresults")){
            //     long chat_id = update.getMessage().getChatId();
            //     String res = "";
            //     for(int i = 0; i < answers.size(); i++){
            //     res += answers.get(i)[0]+"+"+answers.get(i)[1]+"="+answers.get(i)[2];
            //     res+="\n";
            //     }
            //     System.out.print(res);
            //     SendMessage ans = new SendMessage(); // Create a message object objects
            //     ans.setChatId(chat_id);
            //     ans.setText(String.valueOf(res));
            //     try {
            //         execute(ans); // Sending our message object to user
            //     } catch (TelegramApiException e) {
            //         e.printStackTrace();
            //     }
            // }
            else if(message_text.substring(0,8).equals("/addroom")){
                String roomname = message_text.substring(9);
                boolean[][] bookedrooms = new boolean[5][10];
                rooms.add(bookedrooms);
                roomnames.add(roomname);
        
            } 
            else if(message_text.equals("/getroomsList")){
                String res = "";
                for(int i = 0; i < roomnames.size(); i++){
                    res+=(i+1)+" - "+roomnames.get(i)+"\n";
                }
                long chat_id = update.getMessage().getChatId();
                SendMessage ans = new SendMessage();
                ans.setChatId(chat_id);
                ans.setText(res);
                try{
                    execute(ans);
                }
                catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }
            else if(message_text.substring(0,8).equals("/booking")){
                boolean access = true;
                String[] mess = message_text.split(" ");
                int index = Integer.valueOf(mess[2]);
                int day = Integer.valueOf(mess[3]);
                String[] times = message_text.substring(18).split("-");
                int time1 = Integer.valueOf(times[0]);
                int time2 = Integer.valueOf(times[1]);
                long chat_id = update.getMessage().getChatId();
                if((index)<=rooms.size()){
                if(time1 > 8 && time2 < 19 && day > 0 && day  < 6){
                for(int i = time1; i < time2; i++){
                    if(rooms.get(index-1)[day-1][i-9]){
                        access = false;
                        break;
                    }
                }
                    if(!access){
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("This time is already booked, try to choose another");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }    
                }
                else{
                    for(int i = time1; i < time2; i++ ){
                    rooms.get(index-1)[day-1][i-9] = true;
                }
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("You booked room "+roomnames.get(index-1)+" on "+days[day-1]+" from "+time1+":00 till "+time2+":00 successfully");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                }
                else{
                    SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("You can book a room only from monday to friday between 9:00-18:00!");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                SendMessage ans = new SendMessage(); // Create a message object object
                    ans.setChatId(chat_id);
                    ans.setText("Choose room from the list or add it!");
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
            }
            }
            else if(message_text.substring(0,5).equals("/list")){
                String[] mess = message_text.split(" ");
                int day = Integer.valueOf(mess[3]);
                int index = Integer.valueOf(mess[2]); 
                for(int i = 0; i < 9; i++){
                    if(rooms.get(index-1)[day-1][i]){
                        bookinglist[i] = "on "+days[day-1]+" at "+(i+9)+":00 room "+roomnames.get(index-1)+" is booked";
                    }
                    else{
                        bookinglist[i] ="on "+days[day-1]+" at "+(i+9)+":00 room "+roomnames.get(index-1)+" is not booked"; 
                    }
                }
                String res = "";
                for(int i = 0; i < 9; i++){
                    res+=bookinglist[i]+"\n";
                }
                    SendMessage ans = new SendMessage(); // Create a message object object
                    long chat_id = update.getMessage().getChatId();
                    ans.setChatId(chat_id);
                    ans.setText(res);
                    try {
                        execute(ans); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

            }
                
            else{
            System.out.println(message_text + "😀");
            long chat_id = update.getMessage().getChatId();

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
    public void reset(){
        if(Integer.valueOf(java.time.LocalTime.now().toString().substring(0, 2))>=18){
            for(int i = 0; i < 10; i++){
                booked[i] = false;
                bookinglist[i] = "at "+(i+9)+":00 room is not booked";
            }
        }
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