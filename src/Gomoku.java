import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Gomoku {
    private JFrame f = new JFrame("五目並べ Five in a Row");

    BufferedImage table;
    BufferedImage black;
    BufferedImage white;
    BufferedImage selected;

    final int TABLE_WIDTH = 535;
    final int TABLE_HEIGHT = 536;

    // The size of board. 15 horizontally and vertically
    final int BOARD_SIZE = 15;

    // ratio of pieces on the board
    final int RATE = TABLE_WIDTH / BOARD_SIZE;

    // offset of pieces on x and y-axis
    final int X_OFFSET = 5;
    final int Y_OFFSET = 6;

    // Tracking pieces at [x][y], 0-no piece, 1-white, 2-black
    int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

    // Tracking selection frame
    int selected_X = -1;
    int selected_Y = -1;

    // Custom canvas
    private class ChessBoard extends JPanel {
        @Override
        public void paint(Graphics g) {
            // Draw Board
            g.drawImage(table,0,0,null);

            // Draw selection frame
            if (selected_X>0 && selected_Y>0){
                g.drawImage(selected,selected_X*RATE+X_OFFSET,selected_Y*RATE+Y_OFFSET,null);
            }

            // Draw pieces
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    // black piece
                    if (board[i][j] == 2){
                        g.drawImage(black,i*RATE+X_OFFSET,j*RATE+Y_OFFSET,null);
                    }
                    // white piece
                    if (board[i][j] == 1){
                        g.drawImage(white,i*RATE+X_OFFSET,j*RATE+Y_OFFSET,null);
                    }
                }
            }
        }
    }

    ChessBoard chessBoard = new ChessBoard();

    // The current piece
    int board_type = 2;

    // Components in the bottom
    Panel p = new Panel();
    Button whiteBtn = new Button("White");
    Button blackBtn = new Button("Black");
    Button deleteBtn = new Button("Remove");

    public void refreshBtnColor(Color whiteBtnColor,Color blackBtnColor,Color deleteBtnColor){
        whiteBtn.setBackground(whiteBtnColor);
        blackBtn.setBackground(blackBtnColor);
        deleteBtn.setBackground(deleteBtnColor);
    }

    public void init() throws Exception {
        // Construct the view and control logic
        whiteBtn.addActionListener(e->{
            // change the tag of the piece to 1
            board_type = 1;
            // refresh color
            refreshBtnColor(Color.GREEN,Color.GRAY,Color.GRAY);
        });

        blackBtn.addActionListener(e->{
            board_type = 2;
            refreshBtnColor(Color.GRAY,Color.GREEN,Color.GRAY);
        });

        deleteBtn.addActionListener(e->{
            board_type = 0;
            refreshBtnColor(Color.GRAY,Color.GRAY,Color.GREEN);
        });

        p.add(whiteBtn);
        p.add(blackBtn);
        p.add(deleteBtn);

        f.add(p,BorderLayout.SOUTH);

        // Construct board

        // Initialize images
        table = ImageIO.read(new File("img/board.jpg"));
        white = ImageIO.read(new File("img/white.gif"));
        black = ImageIO.read(new File("img/black.gif"));
        selected = ImageIO.read(new File("img/selected.gif"));

        // Handle mouse motion
        chessBoard.addMouseMotionListener(new MouseMotionAdapter() {
            //当鼠标移动时会调用该方法
            @Override
            public void mouseMoved(MouseEvent e) {

                selected_X = (e.getX()-X_OFFSET)/RATE;
                selected_Y = (e.getY()-Y_OFFSET)/RATE;

                chessBoard.repaint();
            }
        });

        // Handle mouse click
        chessBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xPos = (e.getX()-X_OFFSET)/RATE;
                int yPos = (e.getY()-Y_OFFSET)/RATE;

                board[xPos][yPos] = board_type;
                chessBoard.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected_X=-1;
                selected_Y=-1;

                chessBoard.repaint();
            }
        });

        chessBoard.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
        f.add(chessBoard);

        f.pack();
        f.setVisible(true);

    }

    public static void main(String[] args) throws Exception {
        new Gomoku().init();
    }

}