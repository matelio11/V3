package gg.kiteblocks.enchants;

import me.cupjok.multicurrency.api.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryView;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.bukkit.projectiles.ProjectileSource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** KiteBlocks v2: original implementation of screenshot-inspired enchantments, books and scrolls. */
@SuppressWarnings("deprecation")
public final class CustomEnchantsPlugin extends JavaPlugin implements Listener {
  private enum E {
    DECAPITATION("Decapitation",10,"sword,axe","basic","Chance to obtain a defeated player's head"),
    WELLFED("WellFed",5,"sword,axe","basic","Feed yourself when attacking players"),
    TRICKSTER("Trickster",5,"sword","advanced","Shuffle your opponent's hotbar"),
    SUMMONER("Summoner",3,"sword,axe,bow,crossbow,trident,mace","advanced","Chance for extra Ender Eyes from Endermen"),
    SCORCH("Scorch",5,"sword","basic","Ignite and slow your opponent"),
    NIGHTBLADE("Nightblade",5,"sword","advanced","Chance to inflict wither"),
    HANGRY("Hangry",5,"sword","basic","Chance to reduce enemy hunger"),
    EXPERENT("Experent",5,"sword,axe","basic","Extra experience from mobs"),
    CATAPULT("Catapult",5,"sword,axe","advanced","Launch your opponent backward"),
    VENOM("Venom",5,"sword","basic","Chance to poison an enemy"),
    TELEKINESIS("Telekinesis",5,"tool,sword,shears","basic","Send dropped blocks directly to inventory"),
    STRIP("Strip",5,"sword","extreme","Chance to remove an armor piece temporarily"),
    SOUL_EATER("Soul Eater",5,"sword,axe","extreme","Summon an allied wither on rare occasions"),
    OMNISCIENCE("Omniscience",5,"sword","extreme","Chance to steal enemy XP"),
    MULTIKILL("Multikill",5,"sword,axe","extreme","Splash damage on nearby mobs"),
    LEADEN("Leaden",5,"sword","advanced","Deal more damage but take extra damage"),
    ILLUMINATING_TOUCH("Illuminating Touch",5,"sword","advanced","Blind nearby enemies"),
    FREEZE("Freeze",3,"sword","advanced","Slow an enemy with freezing effect"),
    DRAGON_SLAYER("Dragon Slayer",5,"sword","extreme","Extra damage against dragons"),
    COMBO_COUNTER("Combo Counter",5,"sword","extreme","Repeated hits increase damage"),
    BLACKOUT("Blackout",5,"sword","extreme","Chance to blind your opponent"),
    ALACRITY("Alacrity",3,"tool,sword","basic","Permanent haste while holding enchanted item"),
    LIGHTNING_ROD("Lightning Rod",5,"helmet","advanced","Chance to summon lightning when hit"),
    HAIL_OF_ARROWS("Hail Of Arrows",5,"helmet","extreme","Chance for defensive arrows"),
    DEATH_WISH("Death Wish",5,"helmet","extreme","Apply weakness to your killer"),
    CLARITY("Clarity",6,"helmet","advanced","Reduce blindness duration"),
    ARMOUR_SAVER("Armour Saver",1,"armor","basic","Protect worn armor from breaking"),
    SHARK_GILLS("Shark Gills",1,"helmet","basic","Water breathing while equipped"),
    LIFEFORCE("Lifeforce",3,"helmet","advanced","Regenerate food and health"),
    HEAT_REFLECTOR("Heat Reflector",5,"helmet","advanced","Chance to ignite your attacker"),
    EXTRA_PADDING("Extra Padding",10,"armor","advanced","Additional health while equipped"),
    CAT_EYES("Cat Eyes",1,"helmet","basic","Night vision while equipped"),
    ARROW_STORM("Arrow Storm",5,"helmet","extreme","Fire defensive arrows during combat"),
    BLOOD_RAGE("Blood Rage",6,"chestplate","extreme","Strength and speed at low health"),
    PUFFER("Puffer",5,"chestplate","advanced","Chance to summon defensive pufferfish"),
    COLOR_SPRAY("Color Spray",5,"leggings","advanced","Chance to blind attackers"),
    REDIRECT("Redirect",5,"leggings","advanced","Reflect part of incoming damage"),
    GUARDIAN_ANGEL("Guardian Angel",5,"leggings","extreme","Regenerate health during combat"),
    SUPER_JUMP("Super Jump",5,"boots","basic","Jump boost while equipped"),
    HERMES_WINGS("Hermes' Wings",5,"boots","advanced","Speed boost while equipped"),
    AGILE("Agile",1,"boots","basic","Prevents trampling farmland"),
    DASHER("Dasher",6,"boots","extreme","Speed burst while sprinting"),
    AUTO_SMELT("Auto Smelt",5,"pickaxe","basic","Smelt mined ore drops"),
    VEIN_MINER("Vein Miner",3,"pickaxe","advanced","Chance to mine connected ore vein"),
    BLOCK_SPLITTER("Block Splitter",3,"pickaxe","extreme","Chance for additional ore from stone"),
    LUMBERJACK("Lumberjack",2,"axe","basic","Gather connected logs quickly"),
    GREEN_THUMB("Green Thumb",10,"hoe","basic","Replant mature crops on harvest"),
    GREATER_FARMING("Greater Farming",5,"hoe","basic","Bonus separate XP from harvested crops"),
    GATHERER("Gatherer",5,"hoe","advanced","Chance for double crop drops"),
    MULTISHOT("Multishot",5,"bow","advanced","Fire extra arrows with a bow"),
    REPLENISH("Replenish",5,"bow,crossbow","basic","Chance to return an arrow"),
    PRESERVATION("Preservation",4,"bow","basic","Repair held bow gradually"),
    HAUNTED("Haunted",5,"bow,crossbow","extreme","Chance to summon a hostile ghost"),
    FIERY_BARRAGE("Fiery Barrage",5,"bow","extreme","Additional flaming projectiles"),
    DRAGON_HUNTER("Dragon Hunter",5,"bow","extreme","Increase damage against dragon bosses"),
    RAILGUN("Railgun",6,"bow,crossbow","advanced","Chance to fire additional arrows"),
    POISON_SHOT("Poison Shot",5,"bow","advanced","Poison target bosses"),
    FIREBALL("Fireball",5,"bow,crossbow","extreme","Extra burst damage on projectile impact"),
    DRAGON_TRACKER("Dragon Tracker",3,"bow","extreme","Additional shots against dragon bosses"),
    RADIAL_BLAST("Radial Blast",5,"shield","advanced","Push nearby opponents when blocking"),
    BAIT_SAVER("Bait Saver",3,"fishing_rod","basic","Chance to save bait when fishing"),
    BAIT_FINDER("Bait Finder",3,"fishing_rod","basic","Chance for extra fishing loot");

    final String title,slots,tier,desc; final int max;
    E(String title,int max,String slots,String tier,String desc){this.title=title;this.max=max;this.slots=slots;this.tier=tier;this.desc=desc;}
    static E find(String s){for(E e:values()) if(e.name().equalsIgnoreCase(s.replace(' ','_').replace('-','_').replace("'","")))return e;return null;}
  }
  private final Random random=new Random();
  private final Map<UUID,Long> cooldown=new HashMap<>();
  private final Map<UUID,Integer> combos=new HashMap<>();
  private final Set<UUID> shopBusy=new HashSet<>();
  private static final String SHOP="KiteBlocks | Enchant Shop", INFO="KiteBlocks | Custom Enchants", REROLL="KiteBlocks | Book Reroll", GRINDER="KiteBlocks | Book Grinder";
  private static final Set<Integer> INPUT_SLOTS=Set.of(10,11,12);
  private final Set<UUID> processing=new HashSet<>();
  private static final String[] TIERS={"basic","advanced","extreme"};
  private NamespacedKey bookKey,bookLvl,bookChance,bookBreak,scrollKey;
  private Economy economy;

  @Override public void onEnable(){saveDefaultConfig();bookKey=new NamespacedKey(this,"book_enchant");bookLvl=new NamespacedKey(this,"book_level");bookChance=new NamespacedKey(this,"book_success");bookBreak=new NamespacedKey(this,"book_break");scrollKey=new NamespacedKey(this,"scroll");Bukkit.getPluginManager().registerEvents(this,this);setupEconomy();getLogger().info("Loaded "+E.values().length+" enchants, XP shop, books and scrolls.");}
  private void setupEconomy(){
    if(Bukkit.getPluginManager().getPlugin("Vault")!=null){var registration=Bukkit.getServicesManager().getRegistration(Economy.class);if(registration!=null)economy=registration.getProvider();}
    if(economy==null)getLogger().warning("Vault economy unavailable: scroll purchases disabled until Vault + economy provider are installed.");
  }
  private static String prettyEquipment(String raw){
    Map<String,String> labels=Map.ofEntries(Map.entry("sword","Swords"),Map.entry("axe","Axes"),Map.entry("pickaxe","Pickaxes"),Map.entry("hoe","Hoes"),Map.entry("tool","Tools"),Map.entry("helmet","Helmets"),Map.entry("chestplate","Chestplates"),Map.entry("leggings","Leggings"),Map.entry("boots","Boots"),Map.entry("armor","Armor"),Map.entry("bow","Bows"),Map.entry("crossbow","Crossbows"),Map.entry("trident","Tridents"),Map.entry("mace","Maces"),Map.entry("shield","Shields"),Map.entry("shears","Shears"),Map.entry("fishing_rod","Fishing Rods"));
    List<String> names=new ArrayList<>();for(String slot:raw.split(","))names.add(labels.getOrDefault(slot,slot));return String.join(", ",names);
  }
  private NamespacedKey key(E e){return new NamespacedKey(this,"enchant_"+e.name().toLowerCase(Locale.ROOT));}
  private String color(String s){return ChatColor.translateAlternateColorCodes('&',s);}
  private int level(ItemStack item,E e){if(item==null||item.getType().isAir()||!item.hasItemMeta())return 0;Integer value=item.getItemMeta().getPersistentDataContainer().get(key(e),PersistentDataType.INTEGER);return value==null?0:value;}
  private int equipped(Player p,E e){int lvl=0;for(ItemStack armor:p.getInventory().getArmorContents())lvl=Math.max(lvl,level(armor,e));return lvl;}
  private boolean active(double percent){return random.nextDouble()*100<Math.max(0,Math.min(100,percent));}
  private boolean proc(E e,int level){return level>0&&active(getConfig().getDouble("chances."+e.name().toLowerCase(Locale.ROOT),4.0)*level);}
  private boolean ready(UUID id,String action,long waitMs){String index=id+"."+action;UUID key=UUID.nameUUIDFromBytes(index.getBytes(java.nio.charset.StandardCharsets.UTF_8));long now=System.currentTimeMillis();if(now<cooldown.getOrDefault(key,0L)+waitMs)return false;cooldown.put(key,now);return true;}
  private boolean matches(E e,ItemStack item){if(item==null||item.getType().isAir())return false;String s=item.getType().name();for(String x:e.slots.split(",")){boolean ok=switch(x){case "sword"->s.endsWith("_SWORD");case "axe"->s.endsWith("_AXE")&&!s.endsWith("PICKAXE");case "pickaxe"->s.endsWith("_PICKAXE");case "hoe"->s.endsWith("_HOE");case "tool"->s.endsWith("_PICKAXE")||s.endsWith("_AXE")||s.endsWith("_SHOVEL")||s.endsWith("_HOE");case "helmet"->s.endsWith("_HELMET")||s.equals("TURTLE_HELMET");case "chestplate"->s.endsWith("_CHESTPLATE")||s.equals("ELYTRA");case "leggings"->s.endsWith("_LEGGINGS");case "boots"->s.endsWith("_BOOTS");case "armor"->s.endsWith("_HELMET")||s.endsWith("_CHESTPLATE")||s.endsWith("_LEGGINGS")||s.endsWith("_BOOTS")||s.equals("ELYTRA");case "bow"->s.equals("BOW");case "crossbow"->s.equals("CROSSBOW");case "trident"->s.equals("TRIDENT");case "mace"->s.equals("MACE");case "shield"->s.equals("SHIELD");case "shears"->s.equals("SHEARS");case "fishing_rod"->s.equals("FISHING_ROD");default->false;};if(ok)return true;}return false;}
  private void enchant(ItemStack item,E e,int lvl){ItemMeta meta=item.getItemMeta();if(meta==null)return;meta.getPersistentDataContainer().set(key(e),PersistentDataType.INTEGER,lvl);List<String> lore=meta.hasLore()&&meta.getLore()!=null?new ArrayList<>(meta.getLore()):new ArrayList<>();lore.removeIf(s->ChatColor.stripColor(s).startsWith(e.title+" "));lore.add(color("&d"+e.title+" &f"+lvl));meta.setLore(lore);item.setItemMeta(meta);}
  private ItemStack book(E e,int lvl,int success,int breakChance){ItemStack item=new ItemStack(Material.ENCHANTED_BOOK);ItemMeta m=item.getItemMeta();m.setDisplayName(color("&d&l"+e.title+" "+lvl+" &7Book"));m.setLore(List.of(color("&7Tier: &f"+e.tier),color("&bApplies to: &f"+prettyEquipment(e.slots)),color("&aSuccess: "+success+"%"),color("&cBreak chance: "+breakChance+"%"),color("&7"+e.desc),"",color("&eDrag this book onto valid equipment")));m.getPersistentDataContainer().set(bookKey,PersistentDataType.STRING,e.name());m.getPersistentDataContainer().set(bookLvl,PersistentDataType.INTEGER,lvl);m.getPersistentDataContainer().set(bookChance,PersistentDataType.INTEGER,success);m.getPersistentDataContainer().set(bookBreak,PersistentDataType.INTEGER,breakChance);item.setItemMeta(m);return item;}
  private ItemStack scroll(String kind){ItemStack item=new ItemStack(kind.equals("white")?Material.PAPER:Material.BLACK_DYE);ItemMeta m=item.getItemMeta();m.setDisplayName(color(kind.equals("white")?"&f&lWhite Scroll":"&8&lBlack Scroll"));m.setLore(List.of(color(kind.equals("white")?"&7Adds a random 1-10% to book success chance":"&7Subtracts a random 1-10% from book break chance"),color("&eDrag onto an enchantment book")));m.getPersistentDataContainer().set(scrollKey,PersistentDataType.STRING,kind);item.setItemMeta(m);return item;}
  private int price(String tier){return getConfig().getInt("shop."+tier+"-price",tier.equals("basic")?5000:tier.equals("advanced")?20000:50000);}
  private void shop(Player p){Inventory inv=Bukkit.createInventory(null,27,SHOP);int[] slots={11,13,15};Material[] icons={Material.BOOK,Material.ENCHANTED_BOOK,Material.NETHER_STAR};for(int i=0;i<3;i++){String tier=TIERS[i];ItemStack item=new ItemStack(icons[i]);ItemMeta meta=item.getItemMeta();meta.setDisplayName(color("&e&l"+tier.toUpperCase(Locale.ROOT)+" BOOK"));meta.setLore(List.of(color("&7Price: &e"+price(tier)+" XP"),color("&7A random enchantment book"),color("&aClick to purchase")));item.setItemMeta(meta);inv.setItem(slots[i],item);}ItemStack white=scroll("white");ItemStack black=scroll("black");ItemMeta wm=white.getItemMeta();List<String> wl=new ArrayList<>(wm.getLore());wl.add(color("&ePrice: "+getConfig().getInt("shop.white-scroll-price",5000)+" $"));wm.setLore(wl);white.setItemMeta(wm);ItemMeta bm=black.getItemMeta();List<String> bl=new ArrayList<>(bm.getLore());bl.add(color("&ePrice: "+getConfig().getInt("shop.black-scroll-price",5000)+" $"));bm.setLore(bl);black.setItemMeta(bm);inv.setItem(21,white);inv.setItem(23,black);
    inv.setItem(3,button(Material.ANVIL,"&b&lReroll Books",List.of("&7Exchange 3 books of the SAME tier", "&7for one new random book of that tier.","&eClick to open")));
    inv.setItem(5,button(Material.GRINDSTONE,"&a&lBook Grinder",List.of("&7Recycle custom enchant books for XP.","&7Tier-based XP refunds.","&eClick to open")));
    p.openInventory(inv);}
  private ItemStack button(Material material,String title,List<String> lore){ItemStack item=new ItemStack(material);ItemMeta m=item.getItemMeta();m.setDisplayName(color(title));m.setLore(lore.stream().map(this::color).toList());item.setItemMeta(m);return item;}
  private String bookTier(ItemStack item){if(item==null||item.getType()!=Material.ENCHANTED_BOOK||!item.hasItemMeta())return null;String id=item.getItemMeta().getPersistentDataContainer().get(bookKey,PersistentDataType.STRING);E enchant=id==null?null:E.find(id);return enchant==null?null:enchant.tier;}
  private void openConverter(Player p,boolean reroll){Inventory inv=Bukkit.createInventory(null,27,reroll?REROLL:GRINDER);for(int i=0;i<27;i++)if(!INPUT_SLOTS.contains(i)&&i!=16&&i!=22)inv.setItem(i,button(Material.GRAY_STAINED_GLASS_PANE,"&7",List.of()));inv.setItem(16,button(reroll?Material.ANVIL:Material.EXPERIENCE_BOTTLE,reroll?"&b&lREROLL 3 BOOKS":"&a&lGRIND BOOKS",reroll?List.of("&7Put 3 custom enchant books of", "&7the same tier in slots 10, 11, 12.","&eClick to exchange") :List.of("&7Put up to 3 custom enchant books", "&7in slots 10, 11, 12.","&7Basic: "+getConfig().getInt("grinder.basic-xp",1000)+" XP each", "&7Advanced: "+getConfig().getInt("grinder.advanced-xp",4000)+" XP each", "&7Extreme: "+getConfig().getInt("grinder.extreme-xp",10000)+" XP each","&eClick to grind")));inv.setItem(22,button(Material.BARRIER,"&cReturn to Shop",List.of("&7Unused books will be returned")));p.openInventory(inv);}
  private void returnBooks(Player p,Inventory inv){for(int slot:INPUT_SLOTS){ItemStack item=inv.getItem(slot);if(item==null||item.getType().isAir())continue;inv.setItem(slot,null);Map<Integer,ItemStack> extra=p.getInventory().addItem(item);extra.values().forEach(x->p.getWorld().dropItemNaturally(p.getLocation(),x));}}
  private void processConverter(Player p,Inventory inv,boolean reroll){if(!processing.add(p.getUniqueId()))return;
    List<ItemStack> items=new ArrayList<>();List<String> tiers=new ArrayList<>();for(int slot:INPUT_SLOTS){ItemStack item=inv.getItem(slot);if(item==null||item.getType().isAir())continue;String tier=bookTier(item);if(tier==null){p.sendMessage(color("&cOnly KiteBlocks custom enchant books are accepted."));processing.remove(p.getUniqueId());return;}if(item.getAmount()!=1){p.sendMessage(color("&cPlace one book per input slot."));processing.remove(p.getUniqueId());return;}items.add(item.clone());tiers.add(tier);}
    if(reroll){if(items.size()!=3||!tiers.stream().allMatch(t->t.equals(tiers.get(0)))){p.sendMessage(color("&cReroll needs exactly 3 books of the SAME tier."));processing.remove(p.getUniqueId());return;}for(int slot:INPUT_SLOTS)inv.setItem(slot,null);String tier=tiers.get(0);List<E> pool=new ArrayList<>();for(E e:E.values())if(e.tier.equals(tier))pool.add(e);E chosen=pool.get(random.nextInt(pool.size()));ItemStack result=book(chosen,1+random.nextInt(chosen.max),60+random.nextInt(41),random.nextInt(21));Map<Integer,ItemStack> extra=p.getInventory().addItem(result);extra.values().forEach(x->p.getWorld().dropItemNaturally(p.getLocation(),x));p.sendMessage(color("&aRerolled 3 "+tier+" books into &d"+chosen.title+"&a!"));processing.remove(p.getUniqueId());return;}
    if(items.isEmpty()){p.sendMessage(color("&cInsert at least one custom enchant book."));processing.remove(p.getUniqueId());return;}
    MultiCurrencyApi api=MultiCurrencyProvider.find().orElse(null);if(api==null||!api.isEnabled("xp")){p.sendMessage(color("&cXP currency unavailable; nothing was consumed."));processing.remove(p.getUniqueId());return;}
    int reward=tiers.stream().mapToInt(t->Math.max(0,getConfig().getInt("grinder."+t+"-xp",t.equals("basic")?1000:t.equals("advanced")?4000:10000))).sum();if(reward<=0){p.sendMessage(color("&cGrinder rewards not configured."));processing.remove(p.getUniqueId());return;}
    for(int slot:INPUT_SLOTS)inv.setItem(slot,null); // Reserve books before the asynchronous deposit.
    TransactionContext ctx=TransactionContext.of(Actor.plugin("KiteBlocksCustomEnchants"),"Grind "+items.size()+" enchantment books");
    api.deposit(p.getUniqueId(),"xp",BigDecimal.valueOf(reward),ctx).whenComplete((res,error)->Bukkit.getScheduler().runTask(this,()->{
      processing.remove(p.getUniqueId());if(error!=null||res==null||!res.success()){for(ItemStack item:items){Map<Integer,ItemStack> extra=p.getInventory().addItem(item);extra.values().forEach(x->p.getWorld().dropItemNaturally(p.getLocation(),x));}p.sendMessage(color("&cXP deposit failed. All books returned."));return;}p.sendMessage(color("&aRecycled "+items.size()+" books for &e"+reward+" XP&a!"));
    }));
  }
  @EventHandler public void onConverterClose(InventoryCloseEvent ev){if(!(ev.getPlayer() instanceof Player p))return;String title=ev.getView().getTitle();if(title.equals(REROLL)||title.equals(GRINDER))returnBooks(p,ev.getInventory());}
  @EventHandler public void onConverterDrag(InventoryDragEvent ev){if(ev.getView().getTitle().equals(REROLL)||ev.getView().getTitle().equals(GRINDER)){for(int slot:ev.getRawSlots())if(slot<ev.getView().getTopInventory().getSize()){ev.setCancelled(true);return;}}}
  private void info(Player p,int page){Inventory inv=Bukkit.createInventory(null,54,INFO+" #"+page);int start=page*45;E[] all=E.values();for(int i=0;i<45&&start+i<all.length;i++){E e=all[start+i];ItemStack icon=new ItemStack(Material.ENCHANTED_BOOK);ItemMeta m=icon.getItemMeta();m.setDisplayName(color("&d&l"+e.title));m.setLore(List.of(color("&aMax level: &f"+e.max),color("&aEquipment: &f"+prettyEquipment(e.slots)),color("&aTier: &f"+e.tier),color("&7"+e.desc)));icon.setItemMeta(m);inv.setItem(i,icon);}if(start+45<all.length)inv.setItem(53,new ItemStack(Material.ARROW));if(page>0)inv.setItem(45,new ItemStack(Material.ARROW));p.openInventory(inv);}
  @Override public boolean onCommand(CommandSender sender,Command command,String label,String[] args){String name=command.getName().toLowerCase(Locale.ROOT);if(!(sender instanceof Player p)){sender.sendMessage("Use this command in game.");return true;}
    if(name.equals("enchants")){info(p,0);return true;}
    if(name.equals("enchanter")||name.equals("enchshop")){shop(p);return true;}
    if(!p.hasPermission("kiteblocks.enchants.admin")){p.sendMessage(color("&cNo permission."));return true;}
    if(args.length>=3&&args[0].equalsIgnoreCase("vanilla")){
      Enchantment type=switch(args[1].toLowerCase(Locale.ROOT).replace("-","_").replace(" ","_")){
        case "sharpness"->Enchantment.SHARPNESS;case "unbreaking"->Enchantment.UNBREAKING;case "looting"->Enchantment.LOOTING;
        case "fire_aspect","fireaspect"->Enchantment.FIRE_ASPECT;case "protection"->Enchantment.PROTECTION;
        case "bane_of_arthropods","bane","arthropods"->Enchantment.BANE_OF_ARTHROPODS;
        case "smite"->Enchantment.SMITE;default->null;};
      int lvl;try{lvl=Integer.parseInt(args[2]);}catch(NumberFormatException ex){p.sendMessage(color("&cInvalid level."));return true;}
      int max=type==Enchantment.SHARPNESS||type==Enchantment.PROTECTION?7:type==Enchantment.UNBREAKING||type==Enchantment.LOOTING||type==Enchantment.BANE_OF_ARTHROPODS||type==Enchantment.SMITE?5:type==Enchantment.FIRE_ASPECT?4:0;
      if(type==null||lvl<1||lvl>max){p.sendMessage(color("&cUnknown vanilla enchant or level exceeds allowed cap."));return true;}
      ItemStack held=p.getInventory().getItemInMainHand();if(held.getType().isAir()||!type.canEnchantItem(held)){p.sendMessage(color("&cHold compatible equipment in main hand."));return true;}
      ItemMeta meta=held.getItemMeta();meta.addEnchant(type,lvl,true);held.setItemMeta(meta);p.sendMessage(color("&aApplied "+type.getKey().getKey()+" "+lvl+" (unsafe levels/vanilla conflicts allowed)."));return true;
    }
    if(args.length>=3&&args[0].equalsIgnoreCase("apply")){E e=E.find(args[1]);int l;try{l=Integer.parseInt(args[2]);}catch(Exception ex){p.sendMessage("Invalid level");return true;}if(e==null||l<1||l>e.max){p.sendMessage("Invalid enchant or level");return true;}ItemStack held=p.getInventory().getItemInMainHand();if(!matches(e,held)){p.sendMessage("Wrong equipment for "+e.title);return true;}enchant(held,e,l);p.sendMessage(color("&aApplied "+e.title+" "+l));return true;}
    if(args.length>=2&&args[0].equalsIgnoreCase("book")){E e=E.find(args[1]);if(e==null){p.sendMessage("Unknown enchant");return true;}int lvl=1;try{if(args.length>2)lvl=Integer.parseInt(args[2]);}catch(Exception ex){return true;}if(lvl<1||lvl>e.max)return true;p.getInventory().addItem(book(e,lvl,random.nextInt(41)+60,random.nextInt(21)));return true;}
    if(args.length>=2&&args[0].equalsIgnoreCase("scroll")){String kind=args[1].toLowerCase(Locale.ROOT);if(kind.equals("white")||kind.equals("black"))p.getInventory().addItem(scroll(kind));return true;}
    p.sendMessage(color("&e/kbenchant apply <enchant> <level> | book <enchant> [level] | scroll <white|black> | vanilla <name> <level>"));return true;
  }
  private void takePayment(Player p,String tier){if(!shopBusy.add(p.getUniqueId()))return;MultiCurrencyApi api=MultiCurrencyProvider.find().orElse(null);if(api==null||!api.isEnabled("xp")){shopBusy.remove(p.getUniqueId());p.sendMessage(color("&cXP currency is not available."));return;}int cost=price(tier);TransactionContext ctx=TransactionContext.of(Actor.plugin("KiteBlocksCustomEnchants"),"Purchase "+tier+" enchantment book");api.withdraw(p.getUniqueId(),"xp",BigDecimal.valueOf(cost),ctx).whenComplete((res,error)->Bukkit.getScheduler().runTask(this,()->{shopBusy.remove(p.getUniqueId());if(!p.isOnline())return;if(error!=null||res==null||!res.success()){p.sendMessage(color("&cNot enough XP or transaction failed."));return;}List<E> available=new ArrayList<>();for(E e:E.values())if(e.tier.equals(tier))available.add(e);E e=available.get(random.nextInt(available.size()));ItemStack reward=book(e,1+random.nextInt(e.max),60+random.nextInt(41),random.nextInt(21));Map<Integer,ItemStack> left=p.getInventory().addItem(reward);left.values().forEach(i->p.getWorld().dropItemNaturally(p.getLocation(),i));p.sendMessage(color("&aPurchased "+tier+" book: &d"+e.title+"!"));}));}
  private void buyScroll(Player p,String kind){
    if(economy==null){setupEconomy();if(economy==null){p.sendMessage(color("&cVault money economy unavailable. Install Vault and a Vault economy provider."));return;}}
    int cost=getConfig().getInt("shop."+kind+"-scroll-price",5000);
    if(cost<0){p.sendMessage(color("&cInvalid scroll price."));return;}
    if(!economy.has(p, cost)){p.sendMessage(color("&cYou need $"+cost+" to buy this scroll."));return;}
    EconomyResponse response=economy.withdrawPlayer(p,cost);
    if(!response.transactionSuccess()){p.sendMessage(color("&cMoney payment failed: "+response.errorMessage));return;}
    Map<Integer,ItemStack> leftovers=p.getInventory().addItem(scroll(kind));leftovers.values().forEach(x->p.getWorld().dropItemNaturally(p.getLocation(),x));
    p.sendMessage(color("&aPurchased a "+kind+" scroll for $"+cost+"."));
  }
  @EventHandler(ignoreCancelled=true) public void onClick(InventoryClickEvent ev){if(!(ev.getWhoClicked() instanceof Player p))return;String title=ev.getView().getTitle();if(title.startsWith(INFO)){ev.setCancelled(true);int page=0;try{page=Integer.parseInt(title.substring(title.lastIndexOf('#')+1));}catch(Exception ignored){}if(ev.getRawSlot()==53&&page<2)info(p,page+1);else if(ev.getRawSlot()==45&&page>0)info(p,page-1);return;}if(title.equals(REROLL)||title.equals(GRINDER)){
      int slot=ev.getRawSlot();if(ev.isShiftClick()||ev.getClick()==ClickType.NUMBER_KEY||ev.getClick()==ClickType.DOUBLE_CLICK){ev.setCancelled(true);return;}
      if(slot>=27)return; // Player inventory can be clicked normally to pick up books.
      if(INPUT_SLOTS.contains(slot)){
        if(processing.contains(p.getUniqueId())){ev.setCancelled(true);return;}
        ItemStack cursor=ev.getCursor(),current=ev.getCurrentItem();
        if(cursor!=null&&!cursor.getType().isAir()){
          if(bookTier(cursor)==null||cursor.getAmount()!=1){ev.setCancelled(true);p.sendMessage(color("&cPut exactly one KiteBlocks enchant book in each slot."));return;}
          if(current!=null&&!current.getType().isAir()){ev.setCancelled(true);return;}
        }
        return;
      }
      ev.setCancelled(true);if(slot==16)processConverter(p,ev.getInventory(),title.equals(REROLL));else if(slot==22){p.closeInventory();shop(p);}return;
    }
    if(title.equals(SHOP)){ev.setCancelled(true);int slot=ev.getRawSlot();if(slot==11||slot==13||slot==15){p.closeInventory();takePayment(p,slot==11?"basic":slot==13?"advanced":"extreme");}else if(slot==21||slot==23){p.closeInventory();buyScroll(p,slot==21?"white":"black");}else if(slot==3||slot==5){openConverter(p,slot==3);}return;}
    ItemStack cursor=ev.getCursor(),target=ev.getCurrentItem();if(cursor==null||cursor.getType().isAir()||target==null||target.getType().isAir())return;ItemMeta cursorMeta=cursor.getItemMeta();if(cursorMeta==null)return;
    String sk=cursorMeta.getPersistentDataContainer().get(scrollKey,PersistentDataType.STRING);String en=cursorMeta.getPersistentDataContainer().get(bookKey,PersistentDataType.STRING);
    if(sk!=null&&target.hasItemMeta()){ItemMeta m=target.getItemMeta();Integer suc=m.getPersistentDataContainer().get(bookChance,PersistentDataType.INTEGER),br=m.getPersistentDataContainer().get(bookBreak,PersistentDataType.INTEGER);if(suc==null||br==null)return;ev.setCancelled(true);int delta=1+random.nextInt(10);if(sk.equals("white"))suc=Math.min(100,suc+delta);else br=Math.max(0,br-delta);m.getPersistentDataContainer().set(bookChance,PersistentDataType.INTEGER,suc);m.getPersistentDataContainer().set(bookBreak,PersistentDataType.INTEGER,br);List<String> lore=m.getLore()==null?new ArrayList<>():new ArrayList<>(m.getLore());lore.removeIf(s->ChatColor.stripColor(s).startsWith("Success:")||ChatColor.stripColor(s).startsWith("Break chance:"));lore.add(0,color("&cBreak chance: "+br+"%"));lore.add(0,color("&aSuccess: "+suc+"%"));m.setLore(lore);target.setItemMeta(m);ev.setCurrentItem(target);consumeCursor(ev,cursor);p.sendMessage(color("&aScroll applied: "+delta+"% improvement."));return;}
    if(en!=null){E e=E.find(en);if(e==null||!matches(e,target))return;ev.setCancelled(true);Integer lvl=cursorMeta.getPersistentDataContainer().get(bookLvl,PersistentDataType.INTEGER),suc=cursorMeta.getPersistentDataContainer().get(bookChance,PersistentDataType.INTEGER),br=cursorMeta.getPersistentDataContainer().get(bookBreak,PersistentDataType.INTEGER);if(lvl==null||suc==null||br==null)return;if(level(target,e)>=lvl){p.sendMessage(color("&cItem already has this enchant at this level or higher."));return;}if(active(suc)){enchant(target,e,lvl);ev.setCurrentItem(target);p.sendMessage(color("&aApplied "+e.title+" "+lvl));}else{p.sendMessage(color("&cEnchantment failed."));if(active(br)){ev.setCurrentItem(null);p.sendMessage(color("&cYour equipment broke!"));}}consumeCursor(ev,cursor);}
  }
  private void consumeCursor(InventoryClickEvent ev,ItemStack cursor){if(cursor.getAmount()>1){ItemStack next=cursor.clone();next.setAmount(cursor.getAmount()-1);ev.getView().setCursor(next);}else ev.getView().setCursor(null);}
  @EventHandler(ignoreCancelled=true)public void attack(EntityDamageByEntityEvent ev){Player attacker=null;boolean projectile=false;if(ev.getDamager() instanceof Player p)attacker=p;else if(ev.getDamager() instanceof Projectile proj&&proj.getShooter() instanceof Player p){attacker=p;projectile=true;}if(attacker==null)return;ItemStack held=attacker.getInventory().getItemInMainHand();LivingEntity victim=ev.getEntity() instanceof LivingEntity l?l:null;if(victim==null||victim.equals(attacker))return;final Player a=attacker;
    if(projectile){if(victim instanceof EnderDragon){ev.setDamage(ev.getDamage()*(1+0.18*level(held,E.DRAGON_HUNTER)));}if(proc(E.POISON_SHOT,level(held,E.POISON_SHOT))&&victim.getType()==EntityType.ENDER_DRAGON)victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON,60,0));if(proc(E.FIREBALL,level(held,E.FIREBALL)))ev.setDamage(ev.getDamage()+2);}
    else {if(proc(E.WELLFED,level(held,E.WELLFED))){a.setFoodLevel(Math.min(20,a.getFoodLevel()+4));a.setSaturation(Math.min(20,a.getSaturation()+2));}if(proc(E.HANGRY,level(held,E.HANGRY))&&victim instanceof Player v)v.setFoodLevel(Math.max(0,v.getFoodLevel()-3));if(proc(E.NIGHTBLADE,level(held,E.NIGHTBLADE)))victim.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,60,0));if(proc(E.VENOM,level(held,E.VENOM)))victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON,60,0));if(proc(E.SCORCH,level(held,E.SCORCH))){victim.setFireTicks(Math.max(victim.getFireTicks(),70));victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,60,0));}if(proc(E.FREEZE,level(held,E.FREEZE)))victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,60,2));if(proc(E.BLACKOUT,level(held,E.BLACKOUT)))victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,50,0));if(proc(E.CATAPULT,level(held,E.CATAPULT)))victim.setVelocity(victim.getVelocity().add(victim.getLocation().toVector().subtract(a.getLocation().toVector()).normalize().multiply(1.1).setY(0.7)));if(level(held,E.LEADEN)>0)ev.setDamage(ev.getDamage()*(1+0.07*level(held,E.LEADEN)));if(level(held,E.DRAGON_SLAYER)>0&&victim instanceof EnderDragon)ev.setDamage(ev.getDamage()*(1+0.18*level(held,E.DRAGON_SLAYER)));if(level(held,E.COMBO_COUNTER)>0){int combo=combos.merge(a.getUniqueId(),1,(x,y)->Math.min(x+y,10));ev.setDamage(ev.getDamage()*(1+Math.min(combo,5)*0.03*level(held,E.COMBO_COUNTER)));}if(proc(E.MULTIKILL,level(held,E.MULTIKILL))){for(Entity nearby:victim.getNearbyEntities(2.5,2.5,2.5))if(nearby instanceof Mob m&&!nearby.equals(victim))m.damage(1.0+level(held,E.MULTIKILL),a);}if(proc(E.ILLUMINATING_TOUCH,level(held,E.ILLUMINATING_TOUCH)))for(Entity near:victim.getNearbyEntities(4,4,4))if(near instanceof Player p&&!p.equals(a))p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0));if(proc(E.TRICKSTER,level(held,E.TRICKSTER))&&victim instanceof Player v&&ready(v.getUniqueId(),"trickster",12000)){List<ItemStack> items=new ArrayList<>();for(int i=0;i<9;i++)items.add(v.getInventory().getItem(i));Collections.shuffle(items,random);for(int i=0;i<9;i++)v.getInventory().setItem(i,items.get(i));}}
    if(victim instanceof Player v){if(proc(E.HEAT_REFLECTOR,equipped(v,E.HEAT_REFLECTOR)))a.setFireTicks(Math.max(a.getFireTicks(),60));if(proc(E.COLOR_SPRAY,equipped(v,E.COLOR_SPRAY)))a.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0));if(proc(E.REDIRECT,equipped(v,E.REDIRECT)))a.damage(Math.min(3,ev.getDamage()*0.15),v);if(proc(E.LIGHTNING_ROD,equipped(v,E.LIGHTNING_ROD)))v.getWorld().strikeLightningEffect(a.getLocation());if(proc(E.GUARDIAN_ANGEL,equipped(v,E.GUARDIAN_ANGEL)))v.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,60,1));if(proc(E.BLOOD_RAGE,equipped(v,E.BLOOD_RAGE))&&v.getHealth()<v.getMaxHealth()*0.4){v.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,80,0));v.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,80,0));}}
  }
  @EventHandler public void onKill(PlayerDeathEvent ev){Player killer=ev.getEntity().getKiller();if(killer==null)return;ItemStack held=killer.getInventory().getItemInMainHand();if(proc(E.DECAPITATION,level(held,E.DECAPITATION))){ItemStack head=new ItemStack(Material.PLAYER_HEAD);SkullMeta sm=(SkullMeta)head.getItemMeta();sm.setOwningPlayer(ev.getEntity());head.setItemMeta(sm);ev.getDrops().add(head);}if(proc(E.DEATH_WISH,equipped(ev.getEntity(),E.DEATH_WISH)))killer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,100,1));}
  @EventHandler public void mobKill(EntityDeathEvent ev){Player killer=ev.getEntity().getKiller();if(killer==null)return;ItemStack held=killer.getInventory().getItemInMainHand();if(ev.getEntity() instanceof Enderman&&proc(E.SUMMONER,level(held,E.SUMMONER)))ev.getDrops().add(new ItemStack(Material.ENDER_EYE,2));if(level(held,E.EXPERENT)>0)ev.setDroppedExp((int)Math.min(10000,ev.getDroppedExp()*(1+0.2*level(held,E.EXPERENT))));}
  @EventHandler(ignoreCancelled=true)public void breakBlock(BlockBreakEvent ev){Player p=ev.getPlayer();ItemStack held=p.getInventory().getItemInMainHand();Material type=ev.getBlock().getType();int smelt=level(held,E.AUTO_SMELT);if(smelt>0&&proc(E.AUTO_SMELT,smelt)){Material product=switch(type){case IRON_ORE,DEEPSLATE_IRON_ORE->Material.IRON_INGOT;case GOLD_ORE,DEEPSLATE_GOLD_ORE->Material.GOLD_INGOT;case COPPER_ORE,DEEPSLATE_COPPER_ORE->Material.COPPER_INGOT;default->null;};if(product!=null){ev.setDropItems(false);ev.getBlock().getWorld().dropItemNaturally(ev.getBlock().getLocation(),new ItemStack(product));}}int gather=level(held,E.GATHERER);if(gather>0&&proc(E.GATHERER,gather)&&type.name().contains("WHEAT")){ev.getBlock().getWorld().dropItemNaturally(ev.getBlock().getLocation(),new ItemStack(Material.WHEAT,1));}if(level(held,E.TELEKINESIS)>0){Collection<ItemStack> drops=ev.getBlock().getDrops(held,p);ev.setDropItems(false);for(ItemStack drop:drops){Map<Integer,ItemStack> extras=p.getInventory().addItem(drop);extras.values().forEach(x->p.getWorld().dropItemNaturally(p.getLocation(),x));}}}
  @EventHandler(ignoreCancelled=true)public void onInteract(PlayerInteractEvent ev){if(ev.getAction()==org.bukkit.event.block.Action.PHYSICAL&&ev.getClickedBlock()!=null&&ev.getClickedBlock().getType()==Material.FARMLAND&&equipped(ev.getPlayer(),E.AGILE)>0)ev.setCancelled(true);}
  @EventHandler public void onFish(PlayerFishEvent ev){Player p=ev.getPlayer();ItemStack held=p.getInventory().getItemInMainHand();if(ev.getState()==PlayerFishEvent.State.CAUGHT_FISH&&proc(E.BAIT_FINDER,level(held,E.BAIT_FINDER))){p.getInventory().addItem(new ItemStack(Material.COD,1));}}
  @EventHandler public void onMove(PlayerMoveEvent ev){Player p=ev.getPlayer();if(!ready(p.getUniqueId(),"passives",1500))return;int speed=equipped(p,E.HERMES_WINGS),jump=equipped(p,E.SUPER_JUMP),haste=level(p.getInventory().getItemInMainHand(),E.ALACRITY);if(speed>0)p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,50,Math.min(2,speed/2)));if(jump>0)p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST,50,Math.min(2,jump/2)));if(haste>0)p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,50,Math.min(2,haste-1)));if(equipped(p,E.CAT_EYES)>0)p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,250,0));if(equipped(p,E.SHARK_GILLS)>0)p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,80,0));if(equipped(p,E.DASHER)>0&&p.isSprinting())p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,50,Math.min(3,equipped(p,E.DASHER)/2)));}
}
