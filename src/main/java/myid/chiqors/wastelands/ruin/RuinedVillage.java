package myid.chiqors.wastelands.ruin;

import java.util.Random;

import myid.chiqors.wastelands.utils.Rectangle;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class RuinedVillage 
{
	private Building[] structures;
	private Building center;
	private Layout lay;
	
	private int locX;
	private int locZ;
	private int dimension;
	
	public RuinedVillage(World world, int posX, int posZ, int dim, int size, Random rand)
	{
		rand.nextInt(100);
		System.out.println("Size: " + String.valueOf(size));
		this.locX = posX;
		this.locZ = posZ;
		this.dimension = dim;
		
		int numStructures = 1;
		int centerStruct = 0; //0 = none, 1 = well, 2 = stands, 3 = clock tower
		int smallStruct = 1;
		int midStruct = 0;
		int largeStruct = 0;
		
		if (size == 0)
		{
			numStructures = rand.nextInt(4)+3; //3 - 5
			centerStruct = rand.nextInt(2);
			smallStruct = 3;
			midStruct = numStructures - smallStruct;
		}
		else if (size == 1)
		{
			numStructures = rand.nextInt(5)+5; //4 - 7
			centerStruct = rand.nextInt(2)+1;
			smallStruct = 2;
			largeStruct = rand.nextInt(2)+2; // 1 - 2
			midStruct = numStructures - smallStruct - largeStruct;
		}
		else
		{
			numStructures = rand.nextInt(6)+6; // 5 - 9
			centerStruct = 3; //rand.nextInt(2)+2;
			largeStruct = rand.nextInt(3) + 3;
			midStruct = rand.nextInt(3)+2; // 1 - 3
			if ((midStruct + largeStruct) > numStructures)
			{
				midStruct = numStructures - largeStruct;
				smallStruct = 0;
			}
			else
			{
				smallStruct = numStructures - largeStruct - midStruct;
			}
		}

		System.out.println("Buildings: " + String.valueOf(numStructures) + 
				" - S:" + String.valueOf(smallStruct) + " M:" + String.valueOf(midStruct) + 
				" L:" + String.valueOf(largeStruct) + " - C:" + String.valueOf(centerStruct));
		
		if (centerStruct == 0)
		{
			center = null;
		}
		else if (centerStruct == 1)
		{
			center = Building.create(Building.STAND);
		}
		else if (centerStruct == 2)
		{
			center = Building.create(Building.WELL);
		}
		else
		{
			center = Building.create(Building.CLOCK_TOWER);
		}
		
		structures = new Building[numStructures];
		
		for (int i = 0; i < numStructures; i++)
		{
			int build;
			if (i < smallStruct)
			{
				build = 0;
			}
			else if ((i >= smallStruct) && (i < (midStruct + smallStruct)))
			{
				build = 1;
			}
			else
			{
				build = 2;
			}
			structures[i] = Building.create(pickStruct(rand, build));
			while (checkDuplicates(center, structures, i))
			{
				structures[i] = Building.create(pickStruct(rand, build));
			}
		}
		
		lay = new Layout(world, rand, center, structures, posX, posZ, dim, size);
		//road = new Road(center, structures);
							
	}
	
	private boolean checkDuplicates(Building c, Building[] b, int num)
	{
		if (c != null)
		{
			if (b[num].name.equals(c.name))
			{
				return true;
			}
		}
		for (int i = 0; i < num; i++)
		{
			if (b[num].name.equals(b[i].name) && !b[num].duplicate)
			{
				return true;
			}
		}
		return false;
	}
	
	private int pickStruct(Random rand, int i) {
		// TODO Auto-generated method stub
		switch(i)
		{
		case 0:
			switch (rand.nextInt(5))
			{
			case 0:
				return Building.S_HOUSE1;
			case 1:
				return Building.S_HOUSE2;
			case 2:
				return Building.WELL;
			case 3:
				return Building.CHURCH;
			case 4:
				return Building.STAND;
			}
			break;
		case 1:
			switch (rand.nextInt(3))
			{
			case 0:
				return Building.M_HOUSE1;
			case 1:
				return Building.M_HOUSE2;
			case 2:
				return Building.S_FARM;
			}
			break;
		case 2:
			switch (rand.nextInt(6))
			{
			case 0:
				return Building.DINER;
			case 1:
				return Building.HOSPITAL;
			case 2:
				return Building.L_FARM;
			case 3:
				return Building.L_HOUSE1;
			case 4:
				return Building.L_HOUSE2;
			case 5:
				return Building.LIBRARY;
			}
			break;
		}
		return 0;
	}
	
	public void generate(World world, Random random)
	{
		//int h = world.getHeightValue(locX, locZ);
		//for (int i = 0; i < structures.length; i++)
		//{
		//	world.setBlock(this.locX, h + 10 + i, this.locZ, Blocks.redstone_block);
		//}
		
		if (center != null)
		{
			center.generate(world, random, this.lay.cPos.position, this.lay.cPos.rotation);
		}
		
		for (int i = 0; i < this.lay.bPos.length; i++)
		{
			if ((this.lay.bPos[i] != null) && (this.structures[i] != null))
			{
				structures[i].generate(world, random, this.lay.bPos[i].position, this.lay.bPos[i].rotation);
				//generateRect(world, this.lay.bPos[i], 10+i);
			}
		}
	}

	private void generateRect(World world, Rectangle r, int h) 
	{
		for (int i = 0; i < r.length; i++)
		{
			for (int j = 0; j < r.width; j++)
			{
				if (i==0 || j == 0 || i == (r.length - 1) || j == (r.width - 1))
				{
					world.setBlock(r.position.X+j, r.position.Y+h, r.position.Z+i, Blocks.sponge);
				}
			}
		}
		
	}
}
