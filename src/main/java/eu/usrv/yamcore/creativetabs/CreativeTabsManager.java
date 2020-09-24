
package eu.usrv.yamcore.creativetabs;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;


public class CreativeTabsManager
{
  private Map<String, CreativeTabs> _mCreativeTabsMap = new HashMap<>();

  /**
   * Add new creativetab to local registry for later use
   * 
   * @param pCreativeTab
   * @return
   */
  public boolean AddCreativeTab( ModCreativeTab pCreativeTab )
  {
    return _mCreativeTabsMap.putIfAbsent(pCreativeTab.getTabName(), pCreativeTab) == pCreativeTab;
  }

  public boolean AddCreativeTab( CreativeTabs pCreativeTab )
  {
    return _mCreativeTabsMap.putIfAbsent(pCreativeTab.getTabLabel(), pCreativeTab) == pCreativeTab;
  }

  /**
   * Return CreativeTab instance for given name, if found
   * 
   * @param pDefinedCreativeTabName
   * @return
   */
  public CreativeTabs GetCreativeTabInstance( String pDefinedCreativeTabName )
  {
    return _mCreativeTabsMap.getOrDefault(pDefinedCreativeTabName, null);
  }
}
