package DeltaSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class DeltaMain extends JavaPlugin implements TabExecutor {
	
	ChatColor NoticeChatcolor = ChatColor.DARK_GREEN;
	String NoticeName = "[DeltaS] ";
	
	public List<DSChecker> deltaRuns = new ArrayList<DSChecker>();
	
	public List<String> ignoreAxis = Arrays.asList("x", "y", "z", "xy", "yz", "zx", "yx", "zy", "xz", "none");
	
	private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}]");
	private String format(String msg) {
		if(Bukkit.getVersion().contains("1.16")) {
			Matcher match = pattern.matcher(msg);
			while(match.find()) {
				String color = msg.substring(match.start(), match.end());
				msg = msg.replace(color, ChatColor.of(color) + "");
				match = pattern.matcher(msg);
			}
		}
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	
	@Override
	public void onEnable() {
		getCommand("delta").setExecutor(this);
		getCommand("delta").setTabCompleter(this);

		PluginDescriptionFile pdfile = this.getDescription();
		System.out.println(NoticeChatcolor + "==================================================");
		System.out.println(NoticeChatcolor + NoticeName + "������ : FrNomad");
		System.out.println(NoticeChatcolor + NoticeName + pdfile.getName() + " " + pdfile.getVersion() + " ������ ���� �Ϸ��.");
		System.out.println(NoticeChatcolor + "==================================================");
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfile = this.getDescription();
		System.out.println(NoticeChatcolor + "==================================================");
		System.out.println(NoticeChatcolor + NoticeName + pdfile.getName() + " " + pdfile.getVersion() + " ������ ����.");
		System.out.println(NoticeChatcolor + "==================================================");
	}
	
	public List<Player> playerList() {
		List<Player> plist = new ArrayList<Player>();
		for(int i = 0; i < Bukkit.getServer().getWorlds().size(); i++) {
			plist.addAll(Bukkit.getServer().getWorlds().get(i).getPlayers());
		}
		return plist;
	}
	
	
//-------------------<Command>--------------------------------------------------------------------------
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("delta")) {
			if(s instanceof Player) {
				Player p = (Player) s;
				if(errorLength(args, 1, p, "/delta <start|end|help>", false)) {
					if(args[0].equalsIgnoreCase("start")) {
						if(errorLength(args, 3, p, "/delta start <IgnoreAxis> <frequency>", true)) {
							boolean isValid = true;
							for(int i = 0; i < deltaRuns.size(); i++) {
								if(deltaRuns.get(i).getPlayer() == p) {
									isValid = false;
									break;
								}
							}
							
							if(isValid) {
								try {
									int freq = Integer.parseInt(args[2]);
									if(freq > 0 && freq < 41) {
										if(ignoreAxis.contains(args[1])) {
											DSChecker deltach = new DSChecker(p, args[1], Integer.parseInt(args[2]));
											deltaRuns.add(deltach);
											deltach.runTaskTimer(this, 0, Integer.parseInt(args[2]));
											p.sendMessage(format("&f&l[&a&l!&f&l] &r&a�Ÿ� ������ ���۵˴ϴ�"));
										}
										else {
											p.sendMessage(format("&f&l[&c&l!&f&l] &r&c������ ���� ������ �߸��Ǿ����ϴ�"));
										}
									}
									else {
										p.sendMessage(format("&f&l[&c&l!&f&l] &r&c�����ֱ�� 1 ~ 40ƽ���� ���ѵ˴ϴ�"));
									}
								} catch(NumberFormatException e) {
									p.sendMessage(format("&f&l[&c&l!&f&l] &r&c�����ֱ�� ������ �Է��ؾ� �մϴ�"));
								}
							}
							else {
								p.sendMessage(format("&f&l[&c&l!&f&l] &r&c�̹� �������Դϴ�"));
							}
						}
					}
					else if(args[0].equalsIgnoreCase("end")) {
						if(errorLength(args, 1, p, "/delta end", true)) {
							boolean isExist = false;
							int digit = 0;
							for(int i = 0; i < deltaRuns.size(); i++) {
								if(deltaRuns.get(i).getPlayer() == p) {
									isExist = true;
									digit = i;
									break;
								}
							}
							
							if(isExist) {
								DSChecker endds = deltaRuns.get(digit);
								endds.end();
								endds.getPlayer().sendMessage(format("&2�ɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢ�\n&a&l�Ÿ� ������ �Ϸ�Ǿ����ϴ�!\n&r&a&l������ �� : &r&b" + (endds.getIgnoreX() ? "x" : "") + (endds.getIgnoreY() ? "y" : "") + (endds.getIgnoreZ() ? "z" : "") + (!endds.getIgnoreX() && !endds.getIgnoreY() && !endds.getIgnoreZ() ? "����" : "") + "\n&r&a&l�� �̵��Ÿ� : &r&b" + endds.getTotalS() + " &3m &0/ &b" + 3.28*(endds.getTotalS()) + " &3ft\n&r&2�ɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢ�"));;
								endds.cancel();
								deltaRuns.remove(digit);
							}
							else {
								p.sendMessage(format("&f&l[&c&l!&f&l] &r&c���� �Ÿ��� ���������� �ʽ��ϴ�"));
							}
						}	
					}
					else if(args[0].equalsIgnoreCase("help")) {
						if(errorLength(args, 1, p, "/delta help", true)) {
							p.sendMessage(format("&2�ɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢ�\n&r&l&aDeltaDistance ����\n&f&l[&a&l!&f&l] &r&6/delta start <IgnoreAxis> <frequency> : &r&b���õ��� ���� �࿡ ����Ͽ� �̵��Ÿ��� �����մϴ�.\n&f&l[&a&l!&f&l] &r&6/delta end : &r&b�Ÿ� ������ ������ ������� ����մϴ�.\n&f&l[&a&l!&f&l] &r&6/delta help : &r&b�� ����� ���ϴ�.\n&r&2�ɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢɢ�\n"));
						}
					}
					else {
						p.sendMessage(format("&f&l[&c&l!&f&l] &r&c�˸��� ��ɾ ����Ͻʽÿ�"));
					}
				}
			}
			else {
				s.sendMessage(format("&6/" + label + " &c��ɾ�� �÷��̾ ����� �� �ֽ��ϴ�."));
			}
		}
		return true;
	}
	
	private boolean errorLength(String args[], int i, CommandSender s, String usage, boolean more) {
		if(args.length < i) {
			String args1[] = usage.split(" ");
			List<String> cmd = new ArrayList<String>();
			for(int j = 0; j < args1.length; j++) {
				if(j > args.length) {
					cmd.add("&r&6&l" + args1[j]);
				}
				else {
					cmd.add("&r&7" + args1[j]);
				}
			}
			s.sendMessage(format("&c&l������ ��ɾ�:\n&r" + String.join(" ", cmd)));
			return false;
		}
		else if(args.length > i && more) {
			String args1[] = usage.split(" ");
			List<String> args2 = new ArrayList<>(Arrays.asList(args));
			args2.add(0, args1[0]);
			List<String> cmd = new ArrayList<String>();
			for(int j = 0; j < args2.size(); j++) {
				if(j > args1.length - 1) {
					cmd.add("&r&c&l" + args2.get(j));
				}
				else {
					cmd.add("&r&7" + args2.get(j));
				}
			}
			s.sendMessage(format("&c&l�߸��� ��ɾ�:\n&r" + String.join(" ", cmd)));
			return false;
		}
		else return true;
	}
	
//---------------<TAB>--------------------------------------------------
	List<String> arguments = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
		
		
		List<String> arguments = new ArrayList<String>();
		
		if(args.length == 1) {
			arguments.clear();
			arguments.addAll(Arrays.asList("start", "end", "help"));
		}
		else {
			if(args[0].equalsIgnoreCase("start")) {
				if(args.length == 2) {
					arguments.clear();
					arguments.addAll(ignoreAxis);
				}
				else {
					arguments.clear();
				}
			}
			else {
				arguments.clear();
			}
		}
		
		List<String> result = new ArrayList<String>();
		
		for(String a : arguments) {
			if(a.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				result.add(a);
			}
		}
		return result;
		
	}
}