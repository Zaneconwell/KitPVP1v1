package rippin.bullyscraft.com.Requests;

import me.bullyscraft.com.BullyPVP;
import me.bullyscraft.com.Classes.Kit;
import me.bullyscraft.com.Stats.PlayerStatsObject;
import me.bullyscraft.com.Stats.PlayerStatsObjectManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rippin.bullyscraft.com.Arena;
import rippin.bullyscraft.com.Configs.CachedData;
import rippin.bullyscraft.com.KitPVP1v1;

import java.util.UUID;

import static rippin.bullyscraft.com.KitPVP1v1.*;


public class Request {
    private Arena arena;
    private Kit k;
    private String senderUUID;
    private String receiverUUID;
    private int bid;
    private boolean isBid = false;
    private int confirmBidRequest; //set to 1 when someone can accept bid request.
    int taskid;
    private boolean isRunning = false;
    private Request thisRequest;

    public Request(Arena arena, Kit k, Player sender, Player receiver, int bid){
        this.arena = arena;
        this.k = k;
        this.senderUUID = sender.getUniqueId().toString();
        this.receiverUUID = receiver.getUniqueId().toString();
        this.bid = bid;
        thisRequest = this;
    }

    public Request(Arena arena, Kit k, Player sender, Player receiver){
        this.arena = arena;
        this.k = k;
        this.senderUUID = sender.getUniqueId().toString();
        this.receiverUUID = receiver.getUniqueId().toString();
        thisRequest = this;
    }

    public void sendRequest(){
       isRunning = true;
        final Player receiver = Bukkit.getPlayer(UUID.fromString(receiverUUID));
       final Player sender = Bukkit.getPlayer(UUID.fromString(senderUUID));
        if (receiver.isOnline()){
            if (bid != 0 && bid >=1) {
                PlayerStatsObject psoReceiver = PlayerStatsObjectManager.getPSO(receiver, BullyPVP.instance);
                PlayerStatsObject psoSender = PlayerStatsObjectManager.getPSO(sender, BullyPVP.instance);
                if (psoReceiver != null && psoSender != null){
                    if (psoReceiver.getCoins() >= bid && psoSender.getCoins() >= bid){
                        isBid = true;
                        sender.sendMessage(ChatColor.GREEN + "1v1 request has been sent to " + ChatColor.AQUA + receiver.getName());
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Either you or " + receiver.getName() + " do not have enough coins for the bid.");
                        RequestManager.removeRequest(thisRequest);
                        return;
                    }
                }
                else{
                    sender.sendMessage(ChatColor.RED + "Something weird happened. Tell RlPN, this is not supposed to happen.");
                    RequestManager.removeRequest(thisRequest);
                    return;
                }

            receiver.sendMessage(ChatColor.GREEN + sender.getName() + ChatColor.AQUA + " has challenged you to a 1v1 on Arena " + ChatColor.GREEN + getArena().getName()
            + ChatColor.AQUA + " using the Kit " + ChatColor.GREEN + getK().getName() + ChatColor.AQUA + " bidding " + ChatColor.GREEN + bid + ChatColor.AQUA + " coins" +
                    ChatColor.AQUA + ". Accept or deny the 1v1 by doing /1v1 accept " + ChatColor.GREEN + sender.getName() +
                    ChatColor.AQUA + " or /1v1 deny " + ChatColor.GREEN + sender.getName() );
            }
            else {
                sender.sendMessage(ChatColor.GREEN + "1v1 request has been sent to " + ChatColor.AQUA + receiver.getName());
                receiver.sendMessage(ChatColor.GREEN + sender.getName() + ChatColor.AQUA + " has challenged you to a 1v1 on Arena " + ChatColor.GREEN + getArena().getName()
                        + ChatColor.AQUA + " using the Kit " + ChatColor.GREEN + getK().getName() + ChatColor.AQUA
                        + ". Accept or deny the 1v1 by doing /1v1 accept " + ChatColor.GREEN + sender.getName() +
                        ChatColor.AQUA + " or /1v1 deny " + ChatColor.GREEN + sender.getName());
            }
           taskid = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    isRunning = false;
                    receiver.sendMessage(ChatColor.RED + "The 1v1 request from " + sender.getName() + " has expired.");
                    sender.sendMessage(ChatColor.RED + "Your 1v1 request to " + receiver.getName() + " has expired.");
                    RequestManager.removeRequest(thisRequest);
                }
            }, CachedData.requestExpire);

        }
        else {
            sender.sendMessage(ChatColor.RED + "It seems the player has logged off. Retry later.");
            RequestManager.removeRequest(thisRequest);
        }
    }

    public void cancelTask(){
        Bukkit.getServer().getScheduler().cancelTask(taskid);
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Kit getK() {
        return k;
    }

    public void setK(Kit k) {
        this.k = k;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    public String getReceiverUUID() {
        return receiverUUID;
    }

    public void setReceiverUUID(String receiverUUID) {
        this.receiverUUID = receiverUUID;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public boolean isBid () {
        return isBid;
    }

    public int getConfirmBidRequest() { return confirmBidRequest; }

    public void setConfirmBidRequest(int num){
        this.confirmBidRequest = num;
    }
}
