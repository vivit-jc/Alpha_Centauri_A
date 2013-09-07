
//あとでxml形式にして連携させたいー
MAP_WIDTH = 12;
MAP_HEIGHT = 7;
HexTri_WIDTH = MAP_WIDTH * 3;
HexTri_HEIGHT = MAP_HEIGHT * 2 +1;

ID_ERA_ANCIENT = 0;
ID_ERA_CLASSICAL = 1;
ID_ERA_MEDIEVAL = 2;
ID_ERA_RENAISSANCE = 3;
ID_ERA_INDUSTRIAL = 4;
ID_ERA_MODERN = 5;

DECKTYPE_UNIT = 0;
DECKTYPE_RESOURCE = 1;

DeckContents = [//海洋ユニットは海持ってない文明どうするのって処理が面倒なので時代ボーナスから外しました。
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'槍兵',2],
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'弓兵',2],
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'チャリオット',2],
                
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'重装兵',3],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'複合弓兵',3],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'弓騎兵',3],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'カタパルト',3],
                
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'長槍兵',4],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'長弓兵',4],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'騎士',4],

                [DECKTYPE_UNIT,ID_ERA_RENAISSANCE,'戦列兵',5],
                [DECKTYPE_UNIT,ID_ERA_RENAISSANCE,'ライフル兵',5],
                [DECKTYPE_UNIT,ID_ERA_RENAISSANCE,'騎兵隊',5],
                [DECKTYPE_UNIT,ID_ERA_RENAISSANCE,'カノン砲',5],

                [DECKTYPE_UNIT,ID_ERA_INDUSTRIAL,'歩兵',6],
                [DECKTYPE_UNIT,ID_ERA_INDUSTRIAL,'機関銃兵',6],
                [DECKTYPE_UNIT,ID_ERA_INDUSTRIAL,'戦車',6],
                [DECKTYPE_UNIT,ID_ERA_INDUSTRIAL,'長距離砲',6],

                [DECKTYPE_RESOURCE,ID_ERA_ANCIENT,'食料',100,0],
                [DECKTYPE_RESOURCE,ID_ERA_ANCIENT,'生産',100,1],
                [DECKTYPE_RESOURCE,ID_ERA_ANCIENT,'金銭',100,2],
                [DECKTYPE_RESOURCE,ID_ERA_CLASSICAL,'食料',200,0],
                [DECKTYPE_RESOURCE,ID_ERA_CLASSICAL,'生産',200,1],
                [DECKTYPE_RESOURCE,ID_ERA_CLASSICAL,'金銭',200,2],
                [DECKTYPE_RESOURCE,ID_ERA_MEDIEVAL,'食料',400,0],
                [DECKTYPE_RESOURCE,ID_ERA_MEDIEVAL,'生産',400,1],
                [DECKTYPE_RESOURCE,ID_ERA_MEDIEVAL,'金銭',400,2],
                [DECKTYPE_RESOURCE,ID_ERA_RENAISSANCE,'食料',600,0],
                [DECKTYPE_RESOURCE,ID_ERA_RENAISSANCE,'生産',600,1],
                [DECKTYPE_RESOURCE,ID_ERA_RENAISSANCE,'金銭',600,2],
                [DECKTYPE_RESOURCE,ID_ERA_INDUSTRIAL,'食料',800,0],
                [DECKTYPE_RESOURCE,ID_ERA_INDUSTRIAL,'生産',800,1],
                [DECKTYPE_RESOURCE,ID_ERA_INDUSTRIAL,'金銭',800,2],
                [DECKTYPE_RESOURCE,ID_ERA_MODERN,'食料',1000,0],
                [DECKTYPE_RESOURCE,ID_ERA_MODERN,'生産',1000,1],
                [DECKTYPE_RESOURCE,ID_ERA_MODERN,'金銭',1000,2]
                ];
UNITTYPE_SETTLER = 0;
UNITTYPE_MELEE = 1;
UNITTYPE_SHOOTER = 2;
UNITTYPE_MOUNTED = 3;
UNITTYPE_SIEGE = 4;
UNITTYPE_NAVAL = 5;
UNITTYPE_AIR= 6;
UNITABILITY_BuildCity = 0;
UNITTYPE_STRING = ['開拓','歩兵','射撃','機動','砲撃','海軍','空軍'];
UNITTYPE_DESCRIPTION = [
                        '[開拓] 都市の建造が可能',
                        '[突撃] 歩兵ユニットの戦果を下げる',
                        '[隊列] 騎兵ユニットの戦果を下げる',
                        '[射撃] 突撃ユニットの戦果を下げる',
                        '[側面攻撃] 砲撃の効果を下げる',
                        '[砲撃] 戦闘効率を上昇させる',
                        '[制海] 海上支援の効果を下げる',
                        '[海上支援] 戦闘効率を上昇させる',
                        '[妨害] 制海ユニットの戦果を下げる',
                        '[制空] 爆撃の効果を下げる',
                        '[爆撃] 戦闘効率を上昇させる',
                        ''
                        ];
UNIT_DATA_INDEX_ATTACK = 3;
UNIT_DATA_INDEX_ICON = 5;
UNIT_DATA_INDEX_ABILITY = 6;
UNIT_DATA = [//名前、ユニットタイプ、時代、戦闘、ハンマー、アイコン
             
             //防御用歩兵。対機動アンチの能力は時代とともに弱体化していくという。
             ['槍兵',UNITTYPE_MELEE,ID_ERA_ANCIENT,3,50,11,[]],
             ['重装兵',UNITTYPE_MELEE,ID_ERA_CLASSICAL,7,100,21,[]],
             ['長槍兵',UNITTYPE_MELEE,ID_ERA_MEDIEVAL,15,200,31,[]],
             ['戦列兵',UNITTYPE_MELEE,ID_ERA_RENAISSANCE,27,300,41,[]],
             ['歩兵',UNITTYPE_MELEE,ID_ERA_INDUSTRIAL,40,400,51,[]],
             ['戦闘工兵',UNITTYPE_MELEE,ID_ERA_MODERN,60,500,61,[]],
             
             //弓兵などの拠点防衛ユニット。長弓兵と機関銃がチート。
             ['弓兵',UNITTYPE_SHOOTER,ID_ERA_ANCIENT,3,50,12,[]],
             ['複合弓兵',UNITTYPE_SHOOTER,ID_ERA_CLASSICAL,7,100,22,[]],
             ['長弓兵',UNITTYPE_SHOOTER,ID_ERA_MEDIEVAL,15,200,32,[]],
             ['ライフル兵',UNITTYPE_SHOOTER,ID_ERA_RENAISSANCE,27,300,42,[]],
             ['機関銃兵',UNITTYPE_SHOOTER,ID_ERA_INDUSTRIAL,40,400,52,[]],
             ['機械化歩兵',UNITTYPE_SHOOTER,ID_ERA_MODERN,60,500,62,[]],

             //機動部隊。序盤の役立たずっぷりは割とガチ
             ['チャリオット',UNITTYPE_MOUNTED,ID_ERA_ANCIENT,3,50,13,[]],
             ['弓騎兵',UNITTYPE_MOUNTED,ID_ERA_CLASSICAL,7,100,23,[]],
             ['騎士',UNITTYPE_MOUNTED,ID_ERA_MEDIEVAL,15,200,33,[]],
             ['騎兵隊',UNITTYPE_MOUNTED,ID_ERA_RENAISSANCE,27,300,43,[]],
             ['戦車',UNITTYPE_MOUNTED,ID_ERA_INDUSTRIAL,40,400,53,[]],
             ['機甲部隊',UNITTYPE_MOUNTED,ID_ERA_MODERN,60,500,63,[]],
             
             //砲撃。カタパさん頑張って！
             ['投石兵',UNITTYPE_SIEGE,ID_ERA_ANCIENT,2,50,24,[]],
             ['カタパルト',UNITTYPE_SIEGE,ID_ERA_CLASSICAL,5,100,24,[]],
             ['トレブシェット',UNITTYPE_SIEGE,ID_ERA_MEDIEVAL,9,200,24],[],
             ['カノン砲',UNITTYPE_SIEGE,ID_ERA_RENAISSANCE,18,300,44,[]],
             ['長距離砲',UNITTYPE_SIEGE,ID_ERA_INDUSTRIAL,27,400,54,[]],
             ['自走砲',UNITTYPE_SIEGE,ID_ERA_MODERN,40,500,64,[]],

             
             //制海権を握る用ユニット。
             ['ガレー船',UNITTYPE_NAVAL,ID_ERA_ANCIENT,2,100,25,[]],
             ['三段櫂船',UNITTYPE_NAVAL,ID_ERA_CLASSICAL,5,100,25,[]],
             ['フリーゲート',UNITTYPE_NAVAL,ID_ERA_MEDIEVAL,9,300,45,[]],
             ['戦列艦',UNITTYPE_NAVAL,ID_ERA_RENAISSANCE,18,300,45,[]],
             ['戦艦',UNITTYPE_NAVAL,ID_ERA_INDUSTRIAL,27,600,55,[]],
             ['ミサイル艦',UNITTYPE_NAVAL,ID_ERA_MODERN,40,600,65,[]],
             
             //制空ユニット。戦果には大きく影響しません。
             ['******' ,0,0,1,1,1,1,1],
             ['******' ,0,0,1,1,1,1,1],
             ['******' ,0,0,1,1,1,1,1],
             ['******' ,0,0,1,1,1,1,1],
             ['戦闘機',UNITTYPE_AIR,ID_ERA_INDUSTRIAL,4,200,58,[]],
             ['ジェット機',UNITTYPE_AIR,ID_ERA_MODERN,12,400,68,[]],
             
             ['開拓者' ,UNITTYPE_SETTLER,ID_ERA_ANCIENT,1,200,8,[UNITABILITY_BuildCity]]
             
             ];

ID_ERA_ANCIENT = 0;
ID_ERA_CLASSICAL = 1;
ID_ERA_MEDIEVAL = 2;
ID_ERA_RENAISSANCE = 3;
ID_ERA_INDUSTRIAL = 4;
ID_ERA_MODERN = 5;

TILE_OCEAN = 0;
TILE_GRASSLAND = 1;
TILE_PLAIN = 2;
TILE_FOREST = 3;
TILE_DESERT = 4;
TILE_HILLS = 5;
TILE_MOUNTAIN = 6;

HEX_TILE_PRITORITY = [
		[TILE_OCEAN,TILE_OCEAN,TILE_OCEAN,TILE_OCEAN],
		//[TILE_GRASSLAND,TILE_FOREST,TILE_PLAIN,TILE_HILLS],
		[TILE_GRASSLAND,TILE_FOREST,TILE_OCEAN,TILE_HILLS],
		[TILE_PLAIN,TILE_GRASSLAND,TILE_HILLS,TILE_MOUNTAIN],
		[TILE_FOREST,TILE_HILLS,TILE_GRASSLAND,TILE_DESERT],
		[TILE_DESERT,TILE_PLAIN,TILE_GRASSLAND,TILE_OCEAN],
		[TILE_HILLS,TILE_GRASSLAND,TILE_MOUNTAIN,TILE_PLAIN],
		[TILE_MOUNTAIN,TILE_HILLS,TILE_FOREST,TILE_GRASSLAND]
];

function make_world_data(){
	world_hex = new Array(MAP_WIDTH);
	
	var dm_world_hex = new Array(MAP_WIDTH);
	var search_hex_data = new Array;
	for (i = 0; i < MAP_WIDTH; i++) {
		world_hex[i] = new Array(MAP_HEIGHT);
		dm_world_hex[i] = new Array(MAP_HEIGHT);
		search_hex_data.push([i,0,0]);
		search_hex_data.push([i,MAP_HEIGHT - 1,0]);
		for (t = 0; t < MAP_HEIGHT; t++) {
			dm_world_hex[i][t] = -1;
			world_hex[i][t] = 0;
		}
	}
	var continents = rand(3);
	for (i = 0; i < MAP_HEIGHT; i++) {
		if (rand(continents + 2) == 0){
			search_hex_data.push([1,i,0]);
		}
		if (rand(continents + 2) == 0){
			search_hex_data.push([MAP_WIDTH -2,i,0]);
		}
		if (continents > 0){
			search_hex_data.push([MAP_WIDTH / (continents + 1) - 1 + rand(3),i,0]);
		}
		
		search_hex_data.push([0,i,0]);
		search_hex_data.push([MAP_WIDTH -1,i,0]);
	}
	for (i = 0; i < 5; i++) {
		search_hex_data.push([rand(MAP_WIDTH),rand(MAP_HEIGHT),0]);
	}
	for (i = 0; i < search_hex_data.length; i++) {
		dm_world_hex[search_hex_data[i][0]][search_hex_data[i][1]] = 0;
	}
	var nest_count = 0;
	var hang_up = 0;
	//alert(next_hex_data[0]);
	while ((nest_count < MAP_WIDTH * MAP_HEIGHT - 1) && (hang_up < 100) ){
		var next_hex_data = new Array;
		hang_up += 1;
		for (i = 0; i < search_hex_data.length; i++) {
			var x = search_hex_data[i][0];
			var y = search_hex_data[i][1];
			var nest = search_hex_data[i][2];
			if (valid(x,y) && dm_world_hex[x][y] <= 0){
				dm_world_hex[x][y] = nest;
				//nest_count += 1;
				var n_pos = search_next(x+1,y,nest + rand(2) + rand(3) + rand(4) - rand(2) * rand(3));
				if (n_pos != null){next_hex_data.push(n_pos)}
				var n_pos = search_next(x-1,y,nest + rand(2) + rand(3) + rand(4) - rand(2) * rand(3));
				if (n_pos != null){next_hex_data.push(n_pos)}
				var n_pos = search_next(x,y+1,nest + rand(2) + rand(3) + rand(4) - rand(2) * rand(3));
				if (n_pos != null){next_hex_data.push(n_pos)}
				var n_pos = search_next(x,y-1,nest + rand(2) + rand(3) + rand(4) - rand(2) * rand(3));
				if (n_pos != null){next_hex_data.push(n_pos)}
			}
		}
		search_hex_data = next_hex_data;
	}
	var search_land_hex = new Array;
	for (x = 0; x < MAP_WIDTH; x++) {
		for (y = 0; y < MAP_WIDTH; y++) {
			if (valid(x,y)){
				search_land_hex.push([x,y,dm_world_hex[x][y]]);
			}
		}
	}

	search_land_hex.sort(function(a,b){
		if( a[2] < b[2] ) return -1;
		if( a[2] > b[2] ) return 1;
		return 0;
		}
	);
	for (i = 0; i < search_land_hex.length; i++) {
		var data = search_land_hex[i];
		//alert([data,i, Math.floor(i * 100 / search_land_hex.length)])
		if ((i < search_land_hex.length * 0.3) || (data[2] <= 0)){
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_OCEAN); //海
		} else if (i < search_land_hex.length * 0.6){
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_GRASSLAND); //沿岸
		} else if (i < search_land_hex.length * 0.7){
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_FOREST); //標準
		} else if (i < search_land_hex.length * 0.85){
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_PLAIN); //内地
		} else if (i < search_land_hex.length * 0.9){
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_DESERT); //内地
		} else if (i < search_land_hex.length * 0.95){
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_HILLS); //丘陵
		} else {
			world_hex[data[0]][data[1]] = set_hex_tiles(TILE_MOUNTAIN); //高地
		}
	}

	function set_hex_tiles(id){
		var rand_table = [0,0,0,0,0,0,1,1,1,1,2,2,3];
		var result = [id,0];
		while (result.length < 7){
			result.push(rand_table[rand(rand_table.length)]);
		}
		return result;
	}
	function search_next(x,y,nest){
		if (valid(x,y) && dm_world_hex[x][y] < 0){
			return [x,y,Math.max(nest,0)];
		}
		return null ;
	}
}