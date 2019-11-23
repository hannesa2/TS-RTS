package com.projectreddog.tsrts.network;

import java.util.function.Supplier;

import com.projectreddog.tsrts.tileentity.OwnedCooldownTileEntity;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class TESetRallyPointToServer {
	public int fromPosX;
	public int fromPosY;
	public int fromPosZ;

	public int toPosX;
	public int toPosY;
	public int toPosZ;

	public TESetRallyPointToServer(PacketBuffer buf) {
		// DECODE
		this.fromPosX = buf.readInt();
		this.fromPosY = buf.readInt();
		this.fromPosZ = buf.readInt();

		this.toPosX = buf.readInt();
		this.toPosY = buf.readInt();
		this.toPosZ = buf.readInt();

	}

	public TESetRallyPointToServer(int fromPosX, int fromPosY, int fromPosZ, int toPosX, int toPosY, int toPosZ) {
		super();
		this.fromPosX = fromPosX;
		this.fromPosY = fromPosY;
		this.fromPosZ = fromPosZ;

		this.toPosX = toPosX;
		this.toPosY = toPosY;
		this.toPosZ = toPosZ;

	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(fromPosX);
		buf.writeInt(fromPosY);
		buf.writeInt(fromPosZ);

		buf.writeInt(toPosX);
		buf.writeInt(toPosY);
		buf.writeInt(toPosZ);

	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			TileEntity te = ctx.get().getSender().world.getTileEntity(new BlockPos(fromPosX, fromPosY, fromPosZ));
			if (te instanceof OwnedCooldownTileEntity) {
				OwnedCooldownTileEntity ownedCooldownTileEntity = (OwnedCooldownTileEntity) te;
				ownedCooldownTileEntity.setRallyPoint(new BlockPos(toPosX, toPosY, toPosZ));
			}

		});
		ctx.get().setPacketHandled(true);
	}

}