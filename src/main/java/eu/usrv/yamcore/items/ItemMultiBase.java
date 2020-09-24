package eu.usrv.yamcore.items;

import com.google.common.base.Verify;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.auxiliary.enums.ItemRecipeBehaviorEnum;
import eu.usrv.yamcore.creativetabs.CreativeTabsManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ItemMultiBase extends Item {

    public ItemMultiBase() {
        this.hasSubtypes = true;
    }

    CreativeTabs[] creativeTabsArray = new CreativeTabs[0];

    @Override
    public CreativeTabs[] getCreativeTabs() {
        if (FMLCommonHandler.instance().getSide().isClient() && creativeTabsArray.length == 0)
            creativeTabsArray = SupplierTab2MetaMapping
                    .asMap()
                    .keySet()
                    .stream()
                    .map(Supplier::get)
                    .distinct()
                    .toArray(CreativeTabs[]::new);
        return creativeTabsArray;
    }

    private CreativeTabsManager manager;

    public ItemMultiBase(CreativeTabsManager manager) {
        this.hasSubtypes = true;
        this.manager = manager;
    }

    public void addItemBehavior(int id, ItemRecipeBehaviorEnum itemRecipeBehaviorEnum){
        if (this.Index2Name[id] == null)
            return;
        Index2Behavior[id] = itemRecipeBehaviorEnum;
    }

    public int addItemReference(int id, String name, String textureName, String tabs) {
        int size = addItemReference(id, name, textureName);
        if (FMLCommonHandler.instance().getSide().isClient())
            SupplierTab2MetaMapping.put(() -> manager.GetCreativeTabInstance(tabs), size);
        return size;
    }

    public int addItemReference(int id, String name, String textureName, CreativeTabs tabs) {
        int size = addItemReference(id, name, textureName);
        if (FMLCommonHandler.instance().getSide().isClient())
            Tab2MetaMapping.put(tabs, size);
        return size;
    }

    private int addItemReference(int id, String name, String textureName) {
        if (FMLCommonHandler.instance().getSide().isClient())
            Index2TextureName[id] = (ModId+":item"+textureName);
        Index2Name[id] = (name);
        return id;
    }

    public void setModId(String modId) {
        if (FMLCommonHandler.instance().getSide().isClient())
            ModId = modId;
    }

    @SideOnly(Side.CLIENT)
    private String ModId;
    @SideOnly(Side.CLIENT)
    private String[] Index2TextureName;
    @SideOnly(Side.CLIENT)
    private IIcon[] Index2Icon;

    private final byte[] Index2Stacksize;
    private final String[] Index2Name = new String[Short.MAX_VALUE];
    private final ItemRecipeBehaviorEnum[] Index2Behavior = new ItemRecipeBehaviorEnum[Short.MAX_VALUE];

    @SideOnly(Side.CLIENT)
    private ArrayListMultimap<CreativeTabs, Integer> Tab2MetaMapping;

    @SideOnly(Side.CLIENT)
    private ArrayListMultimap<Supplier<CreativeTabs>, Integer> SupplierTab2MetaMapping;

    private void setCreativeTabs() {
        if (FMLCommonHandler.instance().getSide().isClient())
            SupplierTab2MetaMapping.asMap().forEach((x,y) -> Tab2MetaMapping.putAll(x.get(), y));
    }

    {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            Index2Icon = new IIcon[Short.MAX_VALUE];
            Index2TextureName = new String[Short.MAX_VALUE];
            Tab2MetaMapping = ArrayListMultimap.create();
            SupplierTab2MetaMapping = ArrayListMultimap.create();
        }
        Index2Stacksize = new byte[Short.MAX_VALUE];
        Arrays.fill(Index2Stacksize, (byte) 64);
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int meta = p_77667_1_.getItemDamage();
        if (Index2Name[meta] == null)
            return "ErrorIndex";
        return "item."+this.Index2Name[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        if (Tab2MetaMapping.isEmpty())
            this.setCreativeTabs();
        if (Tab2MetaMapping.containsKey(p_150895_2_))
            Tab2MetaMapping.get(p_150895_2_).forEach(dmg -> p_150895_3_.add(new ItemStack(this, 1, dmg)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        for (int i = 0, index2TextureNameLength = Index2TextureName.length; i < index2TextureNameLength; i++) {
            String idx = Index2TextureName[i];
            if (idx != null) {
                Index2Icon[i] = p_94581_1_.registerIcon(idx);
            }
        }
    }

    public Item setMaxStackSize(int id, int stacksize) {
        //noinspection UnstableApiUsage
        Verify.verify(stacksize > 0 && stacksize < 65);
        this.Index2Stacksize[id] = (byte) stacksize;
        return this;
    }


    @Override
    @Deprecated
    public Item setMaxStackSize(int p_77625_1_) {
        throw new NoSuchMethodError("Use SetMaxStackSize int int instead!");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return Index2Icon[p_77617_1_];
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return Index2Stacksize[stack.getItemDamage()];
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
    {
        short meta = (short) stack.getItemDamage();
        ItemRecipeBehaviorEnum expectedBehavior = Optional.ofNullable(this.Index2Behavior[meta]).orElse(ItemRecipeBehaviorEnum.Consume);
        return expectedBehavior != ItemRecipeBehaviorEnum.NoConsumeLeaveInGrid;
    }
}