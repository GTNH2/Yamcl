
package eu.usrv.yamcore.items;


import cpw.mods.fml.common.registry.GameRegistry;
import eu.usrv.yamcore.auxiliary.enums.ItemRecipeBehaviorEnum;
import eu.usrv.yamcore.creativetabs.CreativeTabsManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public class ModMultiBaseItem
{
    private final ItemMultiBase _mItemInstance;

    public ModMultiBaseItem(String modid, String itemRegistryName, CreativeTabsManager TabManager) {
        this._mItemInstance = new ItemMultiBase(TabManager);
        this._mItemInstance.setModId(modid);
        GameRegistry.registerItem(_mItemInstance, itemRegistryName);
    }

    public ItemStack addItem(int id, String texture, CreativeTabs creativeTab) {
        return addItem(id, texture, creativeTab, texture);
    }

    public ItemStack addItem(int id, String texture, CreativeTabs creativeTab, String name) {
        return new ItemStack(this._mItemInstance, 1, this._mItemInstance.addItemReference(id, name, texture, creativeTab));
    }

    public ItemStack addItem(int id, String texture, CreativeTabs creativeTab, String name, ItemRecipeBehaviorEnum behaviorEnum) {
        ItemStack stack = this.addItem(id, texture, creativeTab, name);
        this._mItemInstance.addItemBehavior(stack.getItemDamage(), behaviorEnum);
        return stack;
    }

    public ItemStack addItem(int id, String texture, String creativeTab) {
        return addItem(id, texture, creativeTab, texture);
    }

    public ItemStack addItem(int id, String texture, String creativeTab, String name) {
        return new ItemStack(this._mItemInstance, 1, this._mItemInstance.addItemReference(id, name, texture, creativeTab));
    }

    public ItemStack addItem(int id, String texture, String creativeTab, String name, ItemRecipeBehaviorEnum behaviorEnum) {
        ItemStack stack = this.addItem(id, texture, creativeTab, name);
        this._mItemInstance.addItemBehavior(stack.getItemDamage(), behaviorEnum);
        return stack;
    }
}