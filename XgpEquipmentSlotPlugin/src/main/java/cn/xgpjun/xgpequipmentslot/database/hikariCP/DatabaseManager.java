package cn.xgpjun.xgpequipmentslot.database.hikariCP;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import cn.xgpjun.xgpequipmentslot.utils.ConfigSetting;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DatabaseManager {
    @Getter
    private HikariDataSource dataSource;
    public DatabaseManager() {
        HikariConfig config = new HikariConfig();
        if(!ConfigSetting.mysql){
            String pluginFolder = XgpEquipmentSlot.getInstance().getDataFolder().getAbsolutePath();
            String databasePath = pluginFolder + File.separator + "data.db";
            if(!ConfigSetting.sqlite.equals(""))
                databasePath=ConfigSetting.sqlite;
            config.setJdbcUrl("jdbc:sqlite:" + databasePath);
        }else {
            File configFile = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "database.yml");
            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
            String url ="jdbc:mysql://" + dataConfig.getString("mysql.url") + "/" + dataConfig.getString("mysql.database")+"?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
            config.setJdbcUrl(url);
            config.setUsername(dataConfig.getString("mysql.username"));
            config.setPassword(dataConfig.getString("mysql.password"));
        }
        config.setMaximumPoolSize(50);
        config.setMinimumIdle(5);

        dataSource = new HikariDataSource(config);
        createTable();
//        dropPrimaryKey();
    }
    public void savePlayerSlotInfo(PlayerSlotInfo playerSlotInfo){
        String name = playerSlotInfo.getName();
        UUID player = playerSlotInfo.getPlayer();
        String items = playerSlotInfo.toJson();
        if(name==null||player==null||items==null|| name.equals("")||items.equals(""))
            return;
        try(Connection connection = getDataSource().getConnection()) {
            // 首先检查是否存在目标行
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM XES_PlayerEquipmentData WHERE player = ? AND name = ?"
            );
            selectStatement.setString(1, String.valueOf(player));
            selectStatement.setString(2,name);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // 如果目标行存在，则执行更新操作
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE XES_PlayerEquipmentData SET items = ? WHERE player = ? AND name = ?"
                );
                updateStatement.setString(3, name);
                updateStatement.setString(1, items);
                updateStatement.setString(2, String.valueOf(player));
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                // 如果目标行不存在，则执行插入操作
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO XES_PlayerEquipmentData (player, name, items) VALUES (?, ?, ?)"
                );
                insertStatement.setString(1, String.valueOf(player));
                insertStatement.setString(2, name);
                insertStatement.setString(3, items);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            resultSet.close();
            selectStatement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public PlayerSlotInfo loadPlayerSlotInfo(UUID player, String name){
        try(Connection connection = getDataSource().getConnection()) {
            // 首先检查是否存在目标行
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM XES_PlayerEquipmentData WHERE player = ? AND name = ?"
            );
            selectStatement.setString(1, String.valueOf(player));
            selectStatement.setString(2,name);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                // 如果目标行存在，则执行更新操作
                return (PlayerSlotInfo.fromJson(resultSet.getString("items"), player, name));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return  (new PlayerSlotInfo(name,player,new HashMap<>()));
    }

    public Set<UUID> getAllPlayer(){
        Set<UUID> set = new HashSet<>();
        try(Connection connection = getDataSource().getConnection()) {
            // 首先检查是否存在目标行
            PreparedStatement selectStatement = connection.prepareStatement(
                    "SELECT * FROM XES_PlayerEquipmentData"
            );
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()){
                set.add(UUID.fromString(resultSet.getString("player")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return set;
    }


    private void createTable() {
        try(Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS XES_PlayerEquipmentData (player varchar(40) , name TEXT, items LONGTEXT)";
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("all")
    public void dropPrimaryKey(){
        try(Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement()) {
            String tableName = "XES_PlayerEquipmentData";
            if(ConfigSetting.mysql){
                String alterTableSQL = "ALTER TABLE " + tableName + " MODIFY COLUMN items LONGTEXT";
                String drop = "ALTER TABLE "+tableName+" DROP PRIMARY KEY";

                statement.execute(alterTableSQL);
                statement.execute(drop);
            }else {
                String createTempTableSQL = "CREATE TABLE temp_table AS SELECT player, name,items FROM " + tableName;
                String dropTableSQL = "DROP TABLE " + tableName;
                String renameTableSQL = "ALTER TABLE temp_table RENAME TO " + tableName;

                statement.execute(createTempTableSQL);
                statement.execute(dropTableSQL);
                statement.execute(renameTableSQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close(){
        dataSource.close();
    }

}
