package com.slothwerks.hearthstone.compendiumforhearthstone.models;

import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Eric on 9/3/2014.
 */
public class Card {

    /*
        "id":"EX1_066",
         "name":"Acidic Swamp Ooze",
         "type":"Minion",
         "faction":"Alliance",
         "rarity":"Common",
         "cost":2,
         "attack":3,
         "health":2,
         "text":"<b>Battlecry:</b> Destroy your opponent's weapon.",
         "flavor":"Oozes love Flamenco.  Don't ask.",
         "artist":"Chris Rahn",
         "collectible":true,
         "howToGetGold":"Unlocked at Rogue Level 57.",
         "mechanics":[
            "Battlecry"
         ]
     */

    public static final String BASE_IMAGE_URL_FORMAT =
            "http://wow.zamimg.com/images/hearthstone/cards/enus/original/%s.png";

    public static final String CARD_ID = "CARD_ID";

    protected String mId;
    protected String mName;
    protected CardSet mSet;
    protected String mRace;
    protected Boolean mElite;
    protected PlayerClass mClass;
    protected CardType mType;
    protected Rarity mRarity;
    protected int mCost;
    protected int mAttack;
    protected int mHealth;
    protected String mText;
    protected String mFlavor;
    protected String mArtist;
    protected boolean mCollectible;
    protected String mHowToGet;
    protected String mHowToGetGold;
    protected List<Mechanic> mMechanics;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public CardType getType() {
        return mType;
    }

    public void setType(CardType type) {
        mType = type;
    }

    public Rarity getRarity() {
        return mRarity;
    }

    public void setRarity(Rarity rarity) {
        mRarity = rarity;
    }

    public int getCost() {
        return mCost;
    }

    public void setCost(int cost) {
        mCost = cost;
    }

    public int getAttack() {
        return mAttack;
    }

    public void setAttack(int attack) {
        mAttack = attack;
    }

    public int getHealth() {
        return mHealth;
    }

    public void setHealth(int health) {
        mHealth = health;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getFlavor() {
        return mFlavor;
    }

    public void setFlavor(String flavor) {
        mFlavor = flavor;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public Boolean isCollectible() {
        return mCollectible;
    }

    public void setCollectible(Boolean collectible) {
        mCollectible = collectible;
    }

    public String getHowToGetGold() {
        return mHowToGetGold;
    }

    public void setHowToGetGold(String howToGetGold) {
        mHowToGetGold = howToGetGold;
    }

    public List<Mechanic> getMechanics() {
        return mMechanics;
    }

    public void setMechanics(List<Mechanic> mechanics) {
        mMechanics = mechanics;
    }

    public CardSet getSet() {
        return mSet;
    }

    public void setSet(CardSet set) {
        mSet = set;
    }

    public String getRace() {
        return mRace;
    }

    public void setRace(String race) {
        mRace = race;
    }

    public boolean getElite() {
        return mElite;
    }

    public void setElite(boolean elite) {
        mElite = elite;
    }

    public PlayerClass getPlayerClass() {
        return mClass;
    }

    public void setClass(PlayerClass aClass) {
        mClass = aClass;
    }

    public String getHowToGet() {
        return mHowToGet;
    }

    public void setHowToGet(String howToGet) {
        mHowToGet = howToGet;
    }

    public String getImageUrl()
    {
        return String.format(BASE_IMAGE_URL_FORMAT, mId);
    }

    @Override
    public String toString() {
        return mName;
    }

    public static Card fromJson(JSONObject o, String set) throws JSONException
    {
        Card card = new Card();

        if(o.has("id"))
            card.setId(o.getString("id"));

        if(o.has("name"))
            card.setName(o.getString("name"));

        if(o.has("rarity"))
            card.setRarity(Rarity.valueOf(o.getString("rarity")));

        if(o.has("cost"))
            card.setCost(o.getInt("cost"));

        if(o.has("attack"))
            card.setAttack(o.getInt("attack"));

        if(o.has("health"))
            card.setHealth(o.getInt("health"));

        if(o.has("text")) {

            //clean up the string we get from JSON
            String text = o.getString("text");
            text = text.replace("#", "");
            text = text.replace("$", "");

            card.setText(text);
        }

        if(o.has("type")) {
            try {
                card.setType(CardType.valueOf(o.getString("type")));
            } catch(Exception e) {
                card.setType(CardType.Unknown);
            }
        }

        if(o.has("elite"))
            card.setElite(o.getBoolean("elite"));
        else
            card.setElite(false);

        if(o.has("collectible"))
            card.setCollectible(o.getBoolean("collectible"));

        if(o.has("flavor"))
            card.setFlavor(o.getString("flavor"));

        if(o.has("playerClass"))
        {
            card.setClass(Utility.stringToPlayerClass(o.getString("playerClass")));
        }

        if(set != null) {
            if(set.equals("Curse of Naxxramas"))
                card.setSet(CardSet.Naxxramas);
            else if(set.equals("Debug"))
                card.setSet(CardSet.Debug);
            else
                card.setSet(CardSet.valueOf(set));
        }

        return card;
    }

    /**
     * Used to sort cards by their mana (resource) cost
     */
    public static class CostComparator implements Comparator<CardQuantityPair> {

        @Override
        public int compare(CardQuantityPair lhs, CardQuantityPair rhs) {
            return lhs.getCard().getCost() - rhs.getCard().getCost();
        }
    }

}
