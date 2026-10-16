package com.tbessentials;

import com.tbessentials.item.WorkstationUpgradeItem;
import com.tbessentials.upgrade.AnvilUpgrade;
import com.tbessentials.upgrade.BrewingUpgrade;
import com.tbessentials.upgrade.SmithingUpgrade;
import com.tbessentials.upgrade.StonecutterUpgrade;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TBEssentials.MODID);

    public static final DeferredItem<WorkstationUpgradeItem> ANVIL_UPGRADE = ITEMS.register("anvil_upgrade",
            () -> new WorkstationUpgradeItem(new Item.Properties().stacksTo(1), "anvil_upgrade", AnvilUpgrade.class, AnvilUpgrade::new, false, false));

    public static final DeferredItem<WorkstationUpgradeItem> STONECUTTER_UPGRADE = ITEMS.register("stonecutter_upgrade",
            () -> new WorkstationUpgradeItem(new Item.Properties().stacksTo(1), "stonecutter_upgrade", StonecutterUpgrade.class, StonecutterUpgrade::new, false, false));

    public static final DeferredItem<WorkstationUpgradeItem> SMITHING_TABLE_UPGRADE = ITEMS.register("smithing_table_upgrade",
            () -> new WorkstationUpgradeItem(new Item.Properties().stacksTo(1), "smithing_table_upgrade", SmithingUpgrade.class, SmithingUpgrade::new, false, false));

    public static final DeferredItem<WorkstationUpgradeItem> BREWING_STAND_UPGRADE = ITEMS.register("brewing_stand_upgrade",
            () -> new WorkstationUpgradeItem(new Item.Properties().stacksTo(1), "brewing_stand_upgrade", BrewingUpgrade.class, BrewingUpgrade::new, true, true));
}
