package com.chess.gui;

import com.chess.engige.board.Move;
import com.chess.engige.piece.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.chess.gui.Table.*;

/**
 * Created by Kusibab on 2017-04-10.
 */
public class KilledPiecesPanel extends JPanel {

    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("#0a4d2f");
    private static final Dimension TAKEN_PIECEC_DIMENSION = new Dimension(40,80);
    private final JPanel northPanel;
    private final JPanel southPanel;

    public KilledPiecesPanel(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECEC_DIMENSION);
    }

    public void redo(final MoveLog moveLog){

        southPanel.removeAll();
        northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Move move : moveLog.getMoves()){
            if (move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }else if (takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                } else throw new RuntimeException("Error in KilledPiecesPanel");
            }
        }
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for (final Piece takenPiece : whiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("graphics/killed/" + takenPiece.getPieceAlliance().toString().substring(0, 1) + takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            } catch (final IOException e){
                e.printStackTrace();
            }
        }
        for (final Piece takenPiece : blackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("graphics/killed/" + takenPiece.getPieceAlliance().toString().substring(0, 1) + takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            } catch (final IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }
}
