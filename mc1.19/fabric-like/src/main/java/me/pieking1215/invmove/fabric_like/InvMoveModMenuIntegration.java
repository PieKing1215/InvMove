package me.pieking1215.invmove.fabric_like;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pieking1215.invmove.InvMoveConfig;

public class InvMoveModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return InvMoveConfig::setupCloth;
    }
}