package me.komugino.retranslation;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.json.*;

public final class Retranslation extends JavaPlugin implements Listener {

    public String[] languages = {"en","ja","ru","zh","is","de","it","fr","ko"};

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        if(!getConfig().contains("enabled")){
            getLogger().info("There is no configuration file for retranslation plugin. Trying to create...");
            getConfig().set("enabled", 0);
            getConfig().set("lang_from", "en");
            getConfig().set("lang_to", "ja");
            getConfig().set("api_url", "PUT API KEY HERE");
        }

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Retranslation plugin has enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Retranslation plugin has diabled.");
    }

    public boolean isInternetOk(){
        boolean internetOK = true;

        try {
            URL url = new URL("http://google.com");
            URLConnection con = url.openConnection();
            con.getInputStream();
        } catch (IOException e) {
            internetOK = false;
        }

        return internetOK;
    }

    public JSONObject getJsonFromWebAPI(String url_api){
        String errorJSON = "{\"error\":\"error\"}";

        try{
            URL url = new URL(url_api);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.connect();

            if (con.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

                String str;
                StringBuffer sb = new StringBuffer();
                while ((str = in.readLine()) != null) {
                    sb.append(str);
                }
                in.close();

                JSONObject jsonObj = new JSONObject(sb.toString());

                return jsonObj;
            }
            con.disconnect();
        }
        catch(ProtocolException e){
            e.printStackTrace();
            JSONObject jsonObj = new JSONObject(errorJSON);
            return jsonObj;
        }
        catch (IOException e){
            e.printStackTrace();
            JSONObject jsonObj = new JSONObject(errorJSON);
            return jsonObj;
        }
        catch(JSONException e) {
            e.printStackTrace();
            JSONObject jsonObj = new JSONObject(errorJSON);
            return jsonObj;
        }

        JSONObject jsonObj = new JSONObject(errorJSON);
        return jsonObj;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if(getConfig().getInt("enabled") == 1){
            Player player = event.getPlayer();
            String message = event.getMessage();

            String modifiedMessage = "";

            if(isInternetOk()){
                JSONObject result = getJsonFromWebAPI(getConfig().getString("api_url") + URLEncoder.encode(message)+ "&source=" + getConfig().getString("lang_from") + "&target=" + getConfig().getString("lang_to"));

                if(!result.has("error")){
                    message = URLDecoder.decode(result.getString("result"));
                    result = getJsonFromWebAPI(getConfig().getString("api_url") + URLEncoder.encode(message)+ "&source=" + getConfig().getString("lang_to") + "&target=" + getConfig().getString("lang_from"));

                    if(!result.has("error")){
                        message = URLDecoder.decode(result.getString("result"));
                        modifiedMessage = "Re: " + URLDecoder.decode(result.getString("result"));
                    }
                    else{
                        modifiedMessage = "Org: " + message;
                    }
                }
                else{
                    modifiedMessage = "Org: " + message;
                }
            }
            else{
                modifiedMessage = "Org: " + message;
            }

            event.setMessage(modifiedMessage);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("retranslate")) {
            if(sender.isOp()){
                if(args[0].equals("enable")){
                    getConfig().set("enabled",1);
                    sender.sendMessage("§aRetranslation is ENABLED.");
                }
                else if(args[0].equals("disable")){
                    getConfig().set("enabled", 0);
                    sender.sendMessage("§aRetranslation is DISABLED.");
                }
                else if(args[0].equals("lang_from")){
                    if(Arrays.asList(languages).contains(args[1])) {
                        getConfig().set("lang_from",args[1]);
                        sender.sendMessage("§aSuccessfully changed source language.");
                    }
                    else{
                        sender.sendMessage("§cThat language is not supported.");
                    }
                }
                else if(args[0].equals("lang_to")){
                    if(Arrays.asList(languages).contains(args[1])) {
                        getConfig().set("lang_to",args[1]);
                        sender.sendMessage("§aSuccessfully changed target language.");
                    }
                    else{
                        sender.sendMessage("§cThat language is not supported.");
                    }
                }
                else if(args[0].equals("api_url")){
                    getConfig().set("api_url",args[1]);
                    sender.sendMessage("§aSuccessfully changed webapi URL.");
                }
            }
            else{
                sender.sendMessage("§cYou don't have enough permission.");
            }
        }
        return false;
    }
}
