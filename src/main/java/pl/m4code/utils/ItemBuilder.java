package pl.m4code.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;
    private List<String> lore = new ArrayList<>();
    private OfflinePlayer skullOwner;

    public ItemBuilder(Material material, int amount, short data) {
        this.item = new ItemStack(material, amount, data);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
        if (owner != null && this.meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) this.meta;
            skullMeta.setOwningPlayer(owner);
        }
        return this;
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public ItemBuilder(Material material) {
        this(material, 1, (short) 0);
    }

    public ItemBuilder setName(String name) {
        if (name == null) return this;

        name = TextUtil.fixColor(name);
        this.meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder hideEnchantments() {
        this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }


    public ItemBuilder hideAttributes() {
        this.meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemBuilder showUnbreakable() {
        this.meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemBuilder showDestroys() {
        this.meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        return this;
    }

    public ItemBuilder showPlacedOn() {
        this.meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        this.lore.addAll(Arrays.stream(lore).map(TextUtil::fixColor).toList());
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        this.lore.addAll(lore.stream().map(TextUtil::fixColor).toList());
        return this;
    }

    public ItemBuilder addLorePlaceholder(String from, String to) {
        this.lore = this.lore.stream().map(s -> s.replace(from, to)).toList();
        return this;
    }

    public ItemStack build() {
        this.meta.setLore(this.lore);
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}