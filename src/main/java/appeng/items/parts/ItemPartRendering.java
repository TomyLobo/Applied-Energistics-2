/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2015, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.items.parts;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import appeng.api.util.AEColor;
import appeng.bootstrap.IItemRendering;
import appeng.bootstrap.ItemRenderingCustomizer;
import appeng.client.render.StaticItemColor;
import appeng.core.AppEng;
import appeng.core.features.registries.PartModels;
import appeng.parts.automation.PlaneConnections;
import appeng.parts.automation.PlaneModel;


public class ItemPartRendering extends ItemRenderingCustomizer
{

	private final PartModels partModels;

	private final ItemPart item;

	public ItemPartRendering( PartModels partModels, ItemPart item )
	{
		this.partModels = partModels;
		this.item = item;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void customize( IItemRendering rendering )
	{

		rendering.meshDefinition( this::getItemMeshDefinition );

		rendering.color( new StaticItemColor( AEColor.TRANSPARENT ) );

		// Register all item models as variants so they get loaded
		rendering.variants( Arrays.stream( PartType.values() )
				.filter( f -> f != PartType.InvalidType )
				.flatMap( part -> part.getItemModels().stream() )
				.collect( Collectors.toList() ) );

		// Register the built-in models for annihilation planes
		ResourceLocation annihilationPlaneTexture = new ResourceLocation( AppEng.MOD_ID, "items/part/annihilation_plane" );
		ResourceLocation annihilationPlaneOnTexture = new ResourceLocation( AppEng.MOD_ID, "parts/annihilation_plane_on" );
		ResourceLocation identityAnnihilationPlaneTexture = new ResourceLocation( AppEng.MOD_ID, "items/part/identity_annihilation_plane" );
		ResourceLocation identityAnnihilationPlaneOnTexture = new ResourceLocation( AppEng.MOD_ID, "parts/identity_annihilation_plane_on" );
		ResourceLocation formationPlaneTexture = new ResourceLocation( AppEng.MOD_ID, "items/part/formation_plane" );
		ResourceLocation formationPlaneOnTexture = new ResourceLocation( AppEng.MOD_ID, "parts/formation_plane_on" );
		ResourceLocation sidesTexture = new ResourceLocation( AppEng.MOD_ID, "parts/plane_sides" );
		ResourceLocation backTexture = new ResourceLocation( AppEng.MOD_ID, "parts/transition_plane_back" );

		List<String> modelNames = new ArrayList<>();

		for( PlaneConnections connection : PlaneConnections.PERMUTATIONS )
		{
			PlaneModel model = new PlaneModel( annihilationPlaneTexture, sidesTexture, backTexture, connection );
			rendering.builtInModel( "models/part/annihilation_plane_" + connection.getFilenameSuffix(), model );
			modelNames.add( "part/annihilation_plane_" + connection.getFilenameSuffix() );

			model = new PlaneModel( annihilationPlaneOnTexture, sidesTexture, backTexture, connection );
			rendering.builtInModel( "models/part/annihilation_plane_on_" + connection.getFilenameSuffix(), model );
			modelNames.add( "part/annihilation_plane_on_" + connection.getFilenameSuffix() );

			model = new PlaneModel( identityAnnihilationPlaneTexture, sidesTexture, backTexture, connection );
			rendering.builtInModel( "models/part/identity_annihilation_plane_" + connection.getFilenameSuffix(), model );
			modelNames.add( "part/identity_annihilation_plane_" + connection.getFilenameSuffix() );

			model = new PlaneModel( identityAnnihilationPlaneOnTexture, sidesTexture, backTexture, connection );
			rendering.builtInModel( "models/part/identity_annihilation_plane_on_" + connection.getFilenameSuffix(), model );
			modelNames.add( "part/identity_annihilation_plane_on_" + connection.getFilenameSuffix() );

			model = new PlaneModel( formationPlaneTexture, sidesTexture, backTexture, connection );
			rendering.builtInModel( "models/part/formation_plane_" + connection.getFilenameSuffix(), model );
			modelNames.add( "part/formation_plane_" + connection.getFilenameSuffix() );

			model = new PlaneModel( formationPlaneOnTexture, sidesTexture, backTexture, connection );
			rendering.builtInModel( "models/part/formation_plane_on_" + connection.getFilenameSuffix(), model );
			modelNames.add( "part/formation_plane_on_" + connection.getFilenameSuffix() );

		}

		List<ResourceLocation> partResourceLocs = modelNames.stream()
				.map( name -> new ResourceLocation( AppEng.MOD_ID, name ) )
				.collect( Collectors.toList() );
		partModels.registerModels( partResourceLocs );
	}

	private ModelResourceLocation getItemMeshDefinition( ItemStack is )
	{
		PartType partType = item.getTypeByStack( is );
		int variant = item.variantOf( is.getItemDamage() );
		return partType.getItemModels().get( variant );
	}
}
