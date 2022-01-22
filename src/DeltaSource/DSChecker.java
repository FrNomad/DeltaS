package DeltaSource;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.lang.Math;

public class DSChecker extends BukkitRunnable {

	private boolean ignoreX;
	private boolean ignoreY;
	private boolean ignoreZ;
	private double totalS;
	private double Xz;
	private double Yz;
	private double Zz;
	private double dS;
	private int freq;
	private Player p;
	
	public DSChecker(Player pl, String IgnoreAxis, int freq) {
		totalS = 0;
		this.p = pl;
		Xz = pl.getLocation().getX();
		Yz = pl.getLocation().getY();
		Zz = pl.getLocation().getZ();
		this.freq = freq;
		dS = 0;
		
		switch(IgnoreAxis) {
		case "x":
			this.ignoreX = true;
			this.ignoreY = false;
			this.ignoreZ = false;
			break;
		case "y":
			this.ignoreX = false;
			this.ignoreY = true;
			this.ignoreZ = false;
			break;
		case "z":
			this.ignoreX = false;
			this.ignoreY = false;
			this.ignoreZ = true;
			break;
		case "xy":
			this.ignoreX = true;
			this.ignoreY = true;
			this.ignoreZ = false;
			break;
		case "yx":
			this.ignoreX = true;
			this.ignoreY = true;
			this.ignoreZ = false;
			break;
		case "yz":
			this.ignoreX = false;
			this.ignoreY = true;
			this.ignoreZ = true;
			break;
		case "zy":
			this.ignoreX = false;
			this.ignoreY = true;
			this.ignoreZ = true;
			break;
		case "zx":
			this.ignoreX = true;
			this.ignoreY = false;
			this.ignoreZ = true;
			break;
		case "xz":
			this.ignoreX = true;
			this.ignoreY = false;
			this.ignoreZ = true;
			break;
		case "none":
			this.ignoreX = false;
			this.ignoreY = false;
			this.ignoreZ = false;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void run() {
		dS = Math.sqrt(Math.pow((ignoreX ? 0 : Xz - p.getLocation().getX()), 2.0) + Math.pow((ignoreY ? 0 : Yz - p.getLocation().getY()), 2.0) + Math.pow((ignoreZ ? 0 : Zz - p.getLocation().getZ()), 2.0));
		sendCustomActionbar(p, ChatColor.GREEN + "" + ChatColor.BOLD + "현재 속도 " + ChatColor.WHITE + "" + ChatColor.BOLD + ": " + (double)(Math.round(((double)(dS * 2000 / freq)))/(double)100) + " m/s  |  " + (double)(Math.round(((double)(dS * 7200 / freq)))/(double)100) + " km/h");
		totalS += dS;
		Xz = p.getLocation().getX();
		Yz = p.getLocation().getY();
		Zz = p.getLocation().getZ();
	}
	
	public double getTotalS() {
		return Math.round(this.totalS*(double)1000.0)/(double)1000.0;
	}
	
	public boolean getIgnoreX() {
		return this.ignoreX;
	}
	
	public boolean getIgnoreY() {
		return this.ignoreY;
	}
	
	public boolean getIgnoreZ() {
		return this.ignoreZ;
	}
	
	public Player getPlayer() {
		return this.p;
	}
	
	public void end() {
		dS = Math.sqrt(Math.pow((ignoreX ? 0 : Xz - p.getLocation().getX()), 2.0) + Math.pow((ignoreY ? 0 : Yz - p.getLocation().getY()), 2.0) + Math.pow((ignoreZ ? 0 : Zz - p.getLocation().getZ()), 2.0));
		totalS += dS;
	}
	
	private static void sendCustomActionbar(Player p, String str) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(str));
	}
}
