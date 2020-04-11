package com.walmart.labs.ads.keyword.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walmart.labs.ads.keyword.KeywordApplication;
import com.walmart.labs.ads.keyword.model.ParsedKeywordBid;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Map;

import static com.walmart.labs.ads.keyword.datatype.MatchType.BROAD;
import static com.walmart.labs.ads.keyword.datatype.MatchType.EXACT;
import static com.walmart.labs.ads.keyword.datatype.Tenant.WMT;
import static junit.framework.TestCase.assertFalse;

/**
 * This test needs a local redis set up. It's a integration test rather than a unit tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KeywordApplication.class)
@WebAppConfiguration
public class RemoteKeywordLookupServiceTest {
    @Autowired
    RemoteKeywordLookupService service;

    @Before
    public void setUp() throws JsonProcessingException {
        int agid1 = 257686;
        String keywordBidJson1 = "[{\"keywords\":[\"4211930~1 cup tupperware\",\"4211937~12 cup\",\"4211939~14 cup measure\"," +
                "\"4211946~2 cup measuring cups\",\"4211947~2 cup measuring cups set\"," +
                "\"4211957~4 cup measuring cups\",\"4211958~4 television clear mini measuring cups\"," +
                "\"4211961~8 cup\",\"4211969~anchor hook 1 cup decorate glass measure\"," +
                "\"4211981~clear polycarbonate\",\"4212017~measuring cups dry\"," +
                "\"4211836~1 cup dry measuring cups\",\"4211843~1 cup measure\"," +
                "\"4211851~1 cup of brown sugar\",\"4212023~multi color measuring cups\"," +
                "\"4212026~one cup measuring cups\",\"4212042~sterilite 2 cup measuring cups\"]," +
                "\"matchType\":\"phrase\",\"cpc\":0.32}," +
                "{\"keywords\":[\"4211922~1 cup of butter\"," +
                "\"4211923~1 cup of milk\",\"4211924~1 cup of water\",\"4211925~1 cup plastic container\"," +
                "\"4211926~1 cup plastic measuring cups\",\"4211927~1 cup prep bowl\"," +
                "\"4211928~1 cup scoop\",\"4211929~1 cup sour cream\",\"4211931~1 gallon measure\"," +
                "\"4211932~1 gallon measuring cups\",\"4211933~1 liter measuring cups\"," +
                "\"4211934~10 cup measure bowl\",\"4211935~10 gal garbage bag\"," +
                "\"4211936~112 cup measuring cups\",\"4211938~12 cup measuring cups\"," +
                "\"4211940~14 cup measure spoon\",\"4211941~14 cup measuring cups\"," +
                "\"4211942~18 cup measuring cups\",\"4211943~2 cup dry measuring cups\"," +
                "\"4211944~2 cup glass\",\"4211945~2 cup glass measuring cups\"," +
                "\"4211948~2 cup plastic measuring cups\",\"4211949~2 cup pyrex measuring cups\"," +
                "\"4211950~2.5 gal buckets\",\"4211951~3 cup food storage\",\"4211952~3 cup measure\"," +
                "\"4211953~3 cup measuring cups\",\"4211954~312 inch composite kitchen sinks\"," +
                "\"4211955~34 cup measure spoon\",\"4211956~34 cup measuring cups\"," +
                "\"4211959~6 cup measuring cups\",\"4211960~7 cup measuring cups\",\"4211962~8 cup measure\"," +
                "\"4211963~8 cup measuring cups\",\"4211964~88 cup\",\"4211965~99511314942\"," +
                "\"4211966~a one cup measuring cups\",\"4211967~anchor 1 cup\"," +
                "\"4211968~anchor 1 cup measuring cups\",\"4211970~black measure spoon\"," +
                "\"4211971~blackberry fruit 1 cup\"," +
                "\"4211972~buenisimo melamine multi color measuring cups set\"," +
                "\"4211973~cafetera 1 cup\",\"4211974~cambro\",\"4211975~cambro measure\"," +
                "\"4211976~cambro polycarbonate square food storage container\",\"4211977~cereal for 1 cup\"," +
                "\"4211978~christmas measuring cups\",\"4211979~clear glass measuring cups\"," +
                "\"4211980~clear measuring cups\",\"4211982~clear polycarbonate roll\"," +
                "\"4211983~color measuring cups\",\"4211984~cup measure\",\"4211985~dry cup measure\"," +
                "\"4211986~dry mea\",\"4211987~dry measure\",\"4211988~dry measuring cups\"," +
                "\"4211989~enamel measuring cups\",\"4211990~farberware measuring cups spoon set 10\"," +
                "\"4211991~fresh vegetable 1 cup humm\",\"4211992~glass 1 cup measuring cups\"," +
                "\"4211993~glass measure 1 cup\",\"4211994~glass measuring cups 1 cup\"," +
                "\"4211995~kurera 1 cup\",\"4211996~lock n lock measuring cups\"," +
                "\"4211997~mainstay 1 cup measuring cups\",\"4211998~mea\",\"4211999~measu\"," +
                "\"4212000~measur\",\"4212001~measure\",\"4212002~measure 12 cup\"," +
                "\"4212003~measure set\",\"4212004~measure spoon 1 cup\",\"4212005~measure spoon solid\"," +
                "\"4212006~measure supply\",\"4212007~measure teaspoon\",\"4212008~measuri\"," +
                "\"4212009~measuring cups\",\"4212010~measuring cups 1 cup\"," +
                "\"4212011~measuring cups 18 cup\",\"4212012~measuring cups 2 cup\"," +
                "\"4212013~measuring cups 34 cup\",\"4212014~measuring cups 4 cup\"," +
                "\"4212015~measuring cups and spoon set\",\"4212016~measuring cups clear\"," +
                "\"4212018~measuring cups spoon\"," +
                "\"4212019~measuring cups stainless steel steel steel steel steel\"," +
                "\"4211831~1 8 measuring cups\",\"4211832~1 cup\",\"4211833~1 cup bag\"," +
                "\"4211834~1 cup butter\",\"4211835~1 cup buttermilk\",\"4211837~1 cup flour\"," +
                "\"4211838~1 cup glass\",\"4211839~1 cup glass measure\"," +
                "\"4211840~1 cup liquid measure microwaveable\",\"4211841~1 cup liquid measuring cups\"," +
                "\"4211842~1 cup mea\",\"4211844~1 cup measure glass\",\"4211845~1 cup measure plastic\"," +
                "\"4211846~1 cup measure scoop\",\"4211847~1 cup measure spoon\"," +
                "\"4211848~1 cup measuring cups\",\"4211849~1 cup measuring cups glass\"," +
                "\"4211850~1 cup milk\",\"4212020~measuring cups with 34 cup\"," +
                "\"4212021~measuring cups with steel handle\",\"4212022~mixing spoon\"," +
                "\"4212024~multicolor measuring cups\",\"4212025~one cup glass measuring cups\"," +
                "\"4212027~pioneer woman 4 cup measure\",\"4212028~pioneer woman 4 cup measuring cups\"," +
                "\"4212029~pioneer womangeo 6 cup measuring cups\",\"4212030~plain yogurt 1 cup\"," +
                "\"4212031~plastic 1 cup measuring cups\",\"4212032~plastic cup measure\"," +
                "\"4212033~plastic measure 1 cup\",\"4212034~polycarbonate\"," +
                "\"4212035~polycarbonate measuring cups\",\"4212036~pyrex 1 cup measuring cups\"," +
                "\"4212037~pyrex 2 cup glass measuring cups\",\"4212038~pyrex 2 cup measuring cups\"," +
                "\"4212039~small 1 cup measuring cups\",\"4212040~small measuring cups\"," +
                "\"4212041~small men\",\"4212043~sugar 1 cup\",\"4212044~two cup measuring cups\"," +
                "\"4212045~up to 2 cup dry measure c\",\"4212046~winco polycarbonate measure set\"]," +
                "\"matchType\":\"phrase\",\"cpc\":0.36}," +
                "{\"keywords\":[\"3480066~1 cup measuring cups glass\",\"3480067~1 cup milk\"," +
                "\"3480088~2 cup plastic measuring cups\",\"3480111~blackberry fruit 1 cup\"," +
                "\"3480121~clear polycarbonate\",\"3480133~glass 1 cup measuring cups\"," +
                "\"3480138~mainstay 1 cup measuring cups\",\"3480141~measu\",\"3480144~measure 12 cup\"," +
                "\"3480149~measure supply\",\"3480152~measuring cups\",\"3480187~sugar 1 cup\"]," +
                "\"matchType\":\"broad\",\"cpc\":0.32}," +
                "{\"keywords\":[\"3480056~1 cup glass measuring cups\",\"3480125~cute measuring cups\"," +
                "\"3480169~one cup measure\"],\"matchType\":\"broad\",\"cpc\":0.3}," +
                "{\"keywords\":[\"3480041~14 cup dry measuring cups\",\"3480045~18 cup measure spoon\"," +
                "\"3480139~mainstay 1 cup measuring cups 1 each\",\"3480145~measure bottle\"]," +
                "\"matchType\":\"broad\",\"cpc\":0.34}," +
                "{\"keywords\":[\"3480038~112 cup measuring cups\",\"3480039~12 cup\"," +
                "\"3480040~12 cup measuring cups\",\"3480042~14 cup measure\"," +
                "\"3480043~14 cup measure spoon\",\"3480044~14 cup measuring cups\"," +
                "\"3480046~18 cup measuring cups\",\"3480047~1 8 measuring cups\",\"3480048~1 cup\"," +
                "\"3480049~1 cup bag\",\"3480050~1 cup butter\",\"3480051~1 cup buttermilk\"," +
                "\"3480052~1 cup dry measuring cups\",\"3480053~1 cup flour\",\"3480054~1 cup glass\"," +
                "\"3480055~1 cup glass measure\",\"3480057~1 cup liquid measure microwaveable\"," +
                "\"3480058~1 cup liquid measuring cups\",\"3480059~1 cup mea\",\"3480060~1 cup measure\"," +
                "\"3480061~1 cup measure glass\",\"3480062~1 cup measure plastic\"," +
                "\"3480063~1 cup measure scoop\",\"3480064~1 cup measure spoon\"," +
                "\"3480065~1 cup measuring cups\",\"3480068~1 cup of brown sugar\"," +
                "\"3480069~1 cup of butter\",\"3480070~1 cup of milk\",\"3480071~1 cup of water\"," +
                "\"3480072~1 cup plastic container\",\"3480073~1 cup plastic measuring cups\"," +
                "\"3480074~1 cup prep bowl\",\"3480075~1 cup scoop\",\"3480076~1 cup sour cream\"," +
                "\"3480077~1 cup tupperware\",\"3480078~1 gallon measure\"," +
                "\"3480079~1 gallon measuring cups\",\"3480080~1 liter measuring cups\"," +
                "\"3480081~10 cup measure bowl\",\"3480082~10 gal garbage bag\"," +
                "\"3480083~2 cup dry measuring cups\",\"3480084~2 cup glass\"," +
                "\"3480085~2 cup glass measuring cups\",\"3480086~2 cup measuring cups\"," +
                "\"3480087~2 cup measuring cups set\",\"3480089~2 cup pyrex measuring cups\"," +
                "\"3480090~2.5 gal buckets\",\"3480091~312 inch composite kitchen sinks\"," +
                "\"3480092~34 cup measure spoon\",\"3480093~34 cup measuring cups\"," +
                "\"3480094~3 cup food storage\",\"3480095~3 cup measure\",\"3480096~3 cup measuring cups\"," +
                "\"3480097~4 cup measuring cups\",\"3480098~4 television clear mini measuring cups\"," +
                "\"3480099~6 cup measuring cups\",\"3480100~7 cup measuring cups\",\"3480101~8 cup\"," +
                "\"3480102~8 cup measure\",\"3480103~8 cup measuring cups\",\"3480104~88 cup\"," +
                "\"3480105~99511314942\",\"3480106~a one cup measuring cups\",\"3480107~anchor 1 cup\"," +
                "\"3480108~anchor 1 cup measuring cups\",\"3480109~anchor hook 1 cup decorate glass measure\"," +
                "\"3480110~black measure spoon\",\"3480112~buenisimo melamine multi color measuring cups set\"," +
                "\"3480113~cafetera 1 cup\",\"3480114~cambro\",\"3480115~cambro measure\"," +
                "\"3480116~cambro polycarbonate square food storage container\",\"3480117~cereal for 1 cup\"," +
                "\"3480118~christmas measuring cups\",\"3480119~clear glass measuring cups\"," +
                "\"3480120~clear measuring cups\",\"3480122~clear polycarbonate roll\"," +
                "\"3480123~color measuring cups\",\"3480124~cup measure\"," +
                "\"3480126~dry cup measure\",\"3480127~dry mea\",\"3480128~dry measure\"," +
                "\"3480129~dry measuring cups\",\"3480130~enamel measuring cups\"," +
                "\"3480131~farberware measuring cups spoon set 10\",\"3480132~fresh vegetable 1 cup humm\"," +
                "\"3480134~glass measure 1 cup\",\"3480135~glass measuring cups 1 cup\"," +
                "\"3480136~kurera 1 cup\",\"3480137~lock n lock measuring cups\",\"3480140~mea\"," +
                "\"3480142~measur\",\"3480143~measure\",\"3480146~measure set\"," +
                "\"3480147~measure spoon 1 cup\",\"3480148~measure spoon solid\"," +
                "\"3480150~measure teaspoon\",\"3480151~measuri\",\"3480153~measuring cups 18 cup\"," +
                "\"3480154~measuring cups 1 cup\",\"3480155~measuring cups 2 cup\"," +
                "\"3480156~measuring cups 34 cup\",\"3480157~measuring cups 4 cup\"," +
                "\"3480158~measuring cups and spoon set\",\"3480159~measuring cups clear\"," +
                "\"3480160~measuring cups dry\",\"3480161~measuring cups spoon\"," +
                "\"3480162~measuring cups stainless steel steel steel\"," +
                "\"3480163~measuring cups with 34 cup\",\"3480164~measuring cups with steel handle\"," +
                "\"3480165~mixing spoon\",\"3480166~multi color measuring cups\"," +
                "\"3480167~multicolor measuring cups\",\"3480168~one cup glass measuring cups\"," +
                "\"3480170~one cup measuring cups\",\"3480171~pioneer woman 4 cup measure\"," +
                "\"3480172~pioneer woman 4 cup measuring cups\"," +
                "\"3480173~pioneer womangeo 6 cup measuring cups\",\"3480174~plain yogurt 1 cup\"," +
                "\"3480175~plastic 1 cup measuring cups\",\"3480176~plastic cup measure\"," +
                "\"3480177~plastic measure 1 cup\",\"3480178~polycarbonate\"," +
                "\"3480179~polycarbonate measuring cups\",\"3480180~pyrex 1 cup measuring cups\"," +
                "\"3480181~pyrex 2 cup glass measuring cups\",\"3480182~pyrex 2 cup measuring cups\"," +
                "\"3480183~small 1 cup measuring cups\",\"3480184~small measuring cups\"," +
                "\"3480185~small men\",\"3480186~sterilite 2 cup measuring cups\"," +
                "\"3480188~two cup measuring cups\",\"3480189~up to 2 cup dry measure c\"," +
                "\"3480190~winco polycarbonate measure set\"," +
                "\"3972090~measuring cups stainless steel steel steel steel\"]," +
                "\"matchType\":\"broad\",\"cpc\":0.36}," +
                "{\"keywords\":[\"3441947~cambro\"],\"matchType\":\"exact\",\"cpc\":0.36}]";
        int agid2 = 257686;
        String keywordBidJson2 = "[{\"keywords\":[\"3442058~fox run\"],\"matchType\":\"exact\",\"cpc\":0.3}," +
                "{\"keywords\":[\"3487621~24 cup rubbermaid\",\"3487628~35 cup muffin tin\"," +
                "\"3487633~6 count muffin tin\",\"3487668~english muffin tin\"," +
                "\"3487677~fox run donut cutter plate steel tin\",\"3487705~miffin tindineer plate\"," +
                "\"3487710~mini muffin pan cup\",\"3488223~mini plate and cup\",\"3488229~muffin pan steel\"," +
                "\"3488238~muffin tin pan stainless steel steel steel\"],\"matchType\":\"broad\",\"cpc\":0.3}," +
                "{\"keywords\":[\"3487603~24 cup chocolate\",\"3487604~24 cup culler\"," +
                "\"3487605~24 cup cupcake carrier\",\"3487606~24 cup cupcake pan\"," +
                "\"3487607~24 cup cupcake tin\",\"3487608~24 cup food storage\"," +
                "\"3487609~24 cup food storage container\",\"3487610~24 cup measuring cups\"," +
                "\"3487611~24 cup microwaveable bowl\",\"3487612~24 cup mini muffin pan\"," +
                "\"3487613~24 cup mini muffin pan usa\",\"3487614~24 cup mini muffin tin\"," +
                "\"3487615~24 cup muffin\",\"3487616~24 cup muffin pan\"," +
                "\"3487617~24 cup muffin pan with baking sheet\",\"3487618~24 cup muffin tin\"," +
                "\"3487619~24 cup no sugar and peach fruit\",\"3487620~24 cup pan\"," +
                "\"3487622~24 cup silicone mini muffin cupcake\"," +
                "\"3487623~24 cupcake sheet cake pans\",\"3487624~24 hole muffin tin\"," +
                "\"3487625~24 muffin tin\",\"3487626~24 pan\",\"3487627~24 pc muffin tin\"," +
                "\"3487629~4 cup muffin tin\",\"3487630~4 tin muffin pan\"," +
                "\"3487631~48 cup mini muffin pan\",\"3487632~6 count mini muffin pan\"," +
                "\"3487634~6 cupcake tin\",\"3487635~6 muffin tin\"," +
                "\"3487636~6 well muffin tin\",\"3487637~air fryer muffin tin\"," +
                "\"3487638~angel food cake pans fox run\",\"3487639~bake it better 24 cup muffin\"," +
                "\"3487640~big muffin tin\",\"3487641~biscotti pan\",\"3487642~brilliance 24 cup\"," +
                "\"3487643~cellophane cupcake tin\",\"3487644~ceramic mini muffin tin\"," +
                "\"3487645~ceramic pioneer woman muffin tin\",\"3487646~cereal 24 cup\"," +
                "\"3487647~chicago metallic 17724 mini muffin pan aluminum\"," +
                "\"3487648~chinet cut crystal 24 cup\",\"3487649~container reuseable 24 cup\"," +
                "\"3487650~cup and plate and pan\",\"3487651~cup plate and pan set for kitchen\"," +
                "\"3487652~cup plate utensil\",\"3487653~cup with fox on it\"," +
                "\"3487654~cupcake carrier 24 cup\",\"3487655~cupcake cup and plate\"," +
                "\"3487656~cupcake holder 24 cup\",\"3487657~cupcake holder 24 cup storage\"," +
                "\"3487658~cupcake pan 24\",\"3487659~cupcake pan 24 regular\"," +
                "\"3487660~don chelada spicy red 24 cup\",\"3487661~don chelada spicy red 24 cup case\"," +
                "\"3487662~dunder muffin plate\",\"3487663~easter cup and plate or pastry bag\"," +
                "\"3487664~easy bake oven muffin tin\"," +
                "\"3487665~ecolution bakein 24 mini muffin and cupcake\",\"3487666~edible decor 24 cup\"," +
                "\"3487667~electricalcofe heater 24 cup\",\"3487669~entemeen minimuffin\"," +
                "\"3487670~extra large muffin tin\",\"3487671~fox run\"," +
                "\"3487672~fox run 12 standard muffin pan bun\"," +
                "\"3487673~fox run 4 pc english muffin crumpet\",\"3487674~fox run bake pan\"," +
                "\"3487675~fox run brand 12 cup non\",\"3487676~fox run cupcake cup\"," +
                "\"3487678~fox run mini tart pan\",\"3487679~fox run muffin\"," +
                "\"3487680~fox run muffin cup green\",\"3487681~fox run muffin pan\"," +
                "\"3487682~fox run muffin tin\",\"3487683~fox run nonstick heart muffin pan\"," +
                "\"3487684~fox run pan\",\"3487685~fox run pan rack\"," +
                "\"3487686~fox run stainless steel steel steel muffin pan\"," +
                "\"3487687~fox run tart pan\",\"3487688~giant muffin tin\"," +
                "\"3487689~gotham steel muffin pan\",\"3487690~grease tin pan or cup\"," +
                "\"3487691~heart muffin tin\",\"3487692~individual muffin tin\",\"3487693~jumbo muffin tin\"," +
                "\"3487694~kid cup plate and pan set for\",\"3487695~kid play plate pan cup\"," +
                "\"3487696~large cup muffin tin\",\"3487697~large muffin tin\",\"3487698~lil fox plate\"," +
                "\"3487699~mainstay 24 cup cereal dispenser\",\"3487700~mainstay 24 cup pan\"," +
                "\"3487701~mega muffin tin\",\"3487702~meow mix 24 cup\",\"3487703~metal muffin tin\"," +
                "\"3487704~metal puppy paw muffin tin\",\"3487706~mini cupcake tin\"," +
                "\"3487707~mini doughnut pan 24\",\"3487708~mini mouse cup and plate\"," +
                "\"3487709~mini muffin pan 24\",\"3488218~mini muffin pan fox run\"," +
                "\"3488219~mini muffin tin\",\"3488220~mini muffin tin 36\",\"3488221~mini muffin tin cup\"," +
                "\"3488222~mini muffin tin pan\",\"3488224~mini pudding cup pan\",\"3488225~mini tin cup\"," +
                "\"3488226~monster bakeware 21 x 15 24 cup\",\"3488227~muffin pan 24\"," +
                "\"3488228~muffin pan 24 cup\",\"3488230~muffin plate\",\"3488231~muffin tin\"," +
                "\"3488232~muffin tin 24\",\"3488233~muffin tin cup\",\"3488234~muffin tin holder\"," +
                "\"3488235~muffin tin jumbo\",\"3488236~muffin tin mini\",\"3488237~muffin tin pan\"," +
                "\"3488239~muffin tin that hold 24\",\"3488240~muffin tin with top\"," +
                "\"3488241~muffin top tin\",\"3488242~oxo muffin tin\",\"3488243~pan set muffin tin\"," +
                "\"3488244~party decoration 21 cup\",\"3488245~pan muffin tin\"," +
                "\"3488246~peter pan cup and plate\",\"3488247~petite muffin tin\"," +
                "\"3488248~pioneer woman muffin tin\",\"3488249~play cup pan and plate for kitchen\"," +
                "\"3488250~pyrex muffin tin\",\"3488251~retirement cake plate and cup\"," +
                "\"3488252~reusable muffin tin\",\"3488253~rubbermaid 24 cup\"," +
                "\"3488254~silicone muffin tin\",\"3488255~small mini muffin tin\"," +
                "\"3488256~small muffin tin\",\"3488257~spicy maruchan 24 cup soup\"," +
                "\"3488258~square muffin tin\",\"3488259~stainleest steel mini cup\"," +
                "\"3488260~stainless steel steel steel cupcake pan\"," +
                "\"3488261~stainless steel steel steel mini muffin\"," +
                "\"3488262~stainless steel steel steel mini muffin tin\"," +
                "\"3488263~stainless steel steel steel muffin pan\"," +
                "\"3488264~stainless steel steel steel muffin tin\"," +
                "\"3488265~stainless steel steel thermos 24 cup\"," +
                "\"3488266~star shaped muffin tin\",\"3488267~steel mini pan\"," +
                "\"3488268~steel muffin pan\",\"3488269~steel tin cup\"," +
                "\"3488270~sweet creation bake perfect 24 cup mini\",\"3488271~target cupcake tin\"," +
                "\"3488272~teal cupcake tin\",\"3488273~thermos 24 cup pump\"," +
                "\"3488274~tin cup and plate\",\"3488275~tin plate and cup\"," +
                "\"3488276~tin plate steel open top can\",\"3488277~tin plate steel rectangular jug\"," +
                "\"3488278~usa muffin tin\",\"3488279~wilton 24 cup mini muffin pan\"," +
                "\"3488280~wilton mini muffin tin\",\"3487597~20 cup bundt cake pans\"," +
                "\"3487598~24 count cupcake pan\",\"3487599~24 count muffin pan\"," +
                "\"3487600~24 cup 1 oz mini muffin tray\",\"3487601~24 cup bowl\"," +
                "\"3487602~24 cup cake spongebob\"," +
                "\"3972351~muffin tin pan stainless steel steel steel steel\"," +
                "\"3972352~stainless steel steel steel steel cupcake pan\"," +
                "\"3972353~stainless steel steel steel steel mini muffin\"," +
                "\"3972354~stainless steel steel steel steel mini muffin tin\"," +
                "\"3972355~stainless steel steel steel steel muffin pan\"," +
                "\"3972356~stainless steel steel steel steel muffin tin\"," +
                "\"3972357~stainless steel steel steel thermos 24 cup\"]," +
                "\"matchType\":\"broad\",\"cpc\":0.4}]";

        int agid3 = 123456;
        String keywordBidJson3 = "[{\"keywords\":[\"1~facial tissue\",\"2~cloth\",\"3~paper tissue\"], \"matchType\":\"phrase\",\"cpc\":0.5}," +
                "{\"keywords\":[\"4~chocolate cream\", \"5~nuts\", \"6~cereal crunch\"], \"matchType\":\"broad\",\"cpc\":0.5}," +
                "{\"keywords\":[\"7~facial tissue\", \"8~nuts\", \"9~cereal crunch\"], \"matchType\":\"broad\",\"cpc\":0.75}," +
                "{\"keywords\":[\"10~facial tissue paper\"], \"matchType\":\"exact\",\"cpc\":0.2}]";
        int agid4 = 345221;
        String keywordBidJson4 = "[{\"keywords\":[\"1~facial tissue\",\"2~cloth\",\"3~paper tissue\"], \"matchType\":\"phrase\",\"cpc\":0.5}," +
                "{\"keywords\":[\"10~facial tissue paper\"], \"matchType\":\"exact\",\"cpc\":0.2}," +
                "{\"keywords\":[\"11~facial tissue paper\"], \"matchType\":\"exact\",\"cpc\":0.9}]";
        service.addAdGroupKeywordBid(WMT, agid1, ParsedKeywordBid.parseJson(keywordBidJson1));
        service.addAdGroupKeywordBid(WMT, agid2, ParsedKeywordBid.parseJson(keywordBidJson2));
        service.addAdGroupKeywordBid(WMT, agid3, ParsedKeywordBid.parseJson(keywordBidJson3));
        service.addAdGroupKeywordBid(WMT, agid4, ParsedKeywordBid.parseJson(keywordBidJson4));
    }


    @Test
    public void testLookup1() {
        String query = "facial tissue paper";
        long start = System.nanoTime();
        Map<Integer, ParsedKeywordBid> bestBids = service.queryMatching(WMT, query, new HashSet(){
            {
                add(123456);
                add(345221);
            }
        });
        System.out.println("Lookup costs " + (System.nanoTime() - start) + " ms.");

        ParsedKeywordBid bestBid123 = bestBids.get(123456);
        Assert.assertEquals(bestBid123.getKeywords(), "facial tissue");
        Assert.assertEquals(bestBid123.getMatchType(), BROAD);
        Assert.assertEquals(bestBid123.getCpc(), 0.75, 0.0);

        ParsedKeywordBid bestBid345 = bestBids.get(345221);
        Assert.assertEquals(bestBid345.getKeywords(), "facial tissue paper");
        Assert.assertEquals(bestBid345.getMatchType(), EXACT);
        Assert.assertEquals(bestBid345.getCpc(), 0.9, 0.0);
    }

    @Test
    public void testLookup2() {
        String query = "mini cup";
        long start = System.nanoTime();
        Map<Integer, ParsedKeywordBid> bestBids = service.queryMatching(WMT, query, new HashSet(){
            {
                add(257686);
                add(123456);
            }
        });
        System.out.println("Lookup costs " + (System.nanoTime() - start));

        ParsedKeywordBid bestBid257686 = bestBids.get(257686);
        Assert.assertEquals("steel mini pan", bestBid257686.getKeywords());
        Assert.assertEquals(BROAD, bestBid257686.getMatchType());
        Assert.assertEquals(0.400, bestBid257686.getCpc(), 0.0);

        assertFalse(bestBids.containsKey(123456));
    }
}
