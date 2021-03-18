package de.tubeof.securejoin.listener;

import de.tubeof.securejoin.data.Cache;
import de.tubeof.securejoin.data.Data;
import de.tubeof.securejoin.data.MySQL;
import de.tubeof.securejoin.main.SecureJoin;
import de.tubeof.securejoin.utils.GoogleAuthenticatorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CodeVerifier implements Listener {

    private final Data data = SecureJoin.getData();
    private final Cache cache = SecureJoin.getCache();
    private final MySQL mySQL = SecureJoin.getMySQL();
    private final GoogleAuthenticatorManager googleAuthenticatorManager = SecureJoin.getGoogleAuthenticatorManager();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!googleAuthenticatorManager.isPlayerVerifing(player.getUniqueId())) return;
        String authKey = mySQL.getAuthKey(player.getUniqueId());
        Integer code = Integer.parseInt(event.getMessage());
        if(googleAuthenticatorManager.isCodeValid(authKey, code)) {
            googleAuthenticatorManager.changePlayerVerifyState(player.getUniqueId(), false);
            player.sendMessage(data.getPrefix() + "§aErfolgreich verifiziert!");
            if(!mySQL.isAuthVerified(authKey)) {
                mySQL.updateVerifiedState(authKey, true);
            }
        } else {
            player.sendMessage(data.getPrefix() + "§cDer Code ist falsch!");
        }
    }
}