
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

DeckContents = [
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'斧兵を３体追加',13],
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'槍兵を３体追加',14],
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'弓兵を３体追加',15],
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'騎兵を３体追加',16],
                [DECKTYPE_UNIT,ID_ERA_ANCIENT,'ガレー船を３体追加',19],
                
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'軍団兵を４体追加',26],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'重装歩兵を４体追加',27],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'複合弓兵を４体追加',28],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'弓騎兵を４体追加',29],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'カタパルトを４体追加',30],
                [DECKTYPE_UNIT,ID_ERA_CLASSICAL,'三段櫂船を４体追加',33],
                
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'長剣士を5体追加',39],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'長槍兵を5体追加',40],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'長弓兵を5体追加',41],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'騎士を5体追加',42],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'キャラベル船を5体追加',45],
                [DECKTYPE_UNIT,ID_ERA_MEDIEVAL,'ガレオン船を5体追加',46],

                [DECKTYPE_RESOURCE,ID_ERA_ANCIENT,'食料を100追加',1],
                [DECKTYPE_RESOURCE,ID_ERA_ANCIENT,'生産を100追加',2],
                [DECKTYPE_RESOURCE,ID_ERA_ANCIENT,'金銭を100追加',3],
                [DECKTYPE_RESOURCE,ID_ERA_CLASSICAL,'食料を200追加',1],
                [DECKTYPE_RESOURCE,ID_ERA_CLASSICAL,'生産を200追加',2],
                [DECKTYPE_RESOURCE,ID_ERA_CLASSICAL,'金銭を200追加',3]
                ];
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