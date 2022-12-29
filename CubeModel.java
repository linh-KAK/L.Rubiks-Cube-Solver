import java.util.*;

public class CubeModel {

    public static void LinkFace(CubeFace faceA, CubeFace faceN, int orient, int thisR, int nextR){
        faceA.nexts[orient] = faceN;
        faceA.ThisRotations[orient] = thisR;
        faceA.NextRotations[orient] = nextR;       
    }

    public static CubeFace TurnFace(CubeFace src, int turn){
        CubeFace temp = new CubeFace("_");
        temp.face[1][1] = src.face[1][1];
        for(int j = 0; j<turn; j++){
            for(int i = 0; i<3; i++){ //counter-clockwise
                temp.face[0][i] = src.face[i][2];
                temp.face[i][2] = src.face[2][2-i];
                temp.face[2][i] = src.face[i][0];
                temp.face[i][0] = src.face[0][2-i];
            }
            copyFace(src, temp);
        }        
        return src;
    }

    public static void moveblocks(CubeFace ReverseStart,CubeFace nextBlock,int Dir, int RowCol){
        ReverseStart = TurnFace(ReverseStart, ReverseStart.ThisRotations[Dir]);
        nextBlock = TurnFace( nextBlock,  ReverseStart.NextRotations[Dir]);

        for(int i = 0; i<CubeFace.Fsize; i++){
            ReverseStart.face[RowCol][i] = nextBlock.face[RowCol][i];
        }           
        ReverseStart = TurnFace(ReverseStart, 4-ReverseStart.ThisRotations[Dir]);
        nextBlock = TurnFace(nextBlock,  4-ReverseStart.NextRotations[Dir]);
    }

    public static CubeFace movethree(CubeFace Archive, CubeFace ReverseStart,CubeFace Origin, int Dir, int DirStore, int RowCol){
        int turns;
        CubeFace Face;
        if(ReverseStart.nexts[Dir] == Origin ){
            moveblocks(ReverseStart, Archive ,Dir, RowCol);
            Dir = DirStore;
            if(RowCol == 0){    
                ReverseStart = Origin;
                while(ReverseStart.nexts[1-Dir]!=Origin){
                    CubeFace store = ReverseStart;
                    ReverseStart=ReverseStart.nexts[1-Dir];
                    if(ReverseStart.isLR && (store.face[1][1].equals("W")||store.face[1][1].equals("Y"))){
                        Dir = 1-Dir;
                    }else if((ReverseStart.face[1][1].equals("W")||ReverseStart.face[1][1].equals("Y")) && store.isLR){
                        Dir = 1-Dir; 
                    }
                }
                Face = ReverseStart;
                turns = 1;
            }else{        
                Face = Origin.nexts[1-Dir];  
                turns = 3; 
            }
            if(Face.face[1][1].equals("G") || Face.face[1][1].equals("B") || Face.face[1][1].equals("Y")){
                turns = 4 - turns;
            }
            copyFace(Face, TurnFace(Face, turns)); 
            return null;
        }else{
            if(ReverseStart == Origin){
                Archive = new CubeFace("0");
                copyFace(Archive,Origin);
            }
            moveblocks(ReverseStart,ReverseStart.nexts[Dir], Dir, RowCol);
            
            CubeFace store = ReverseStart;
            ReverseStart = ReverseStart.nexts[Dir];
            if(ReverseStart.isLR && (store.face[1][1].equals("W")||store.face[1][1].equals("Y"))){
               Dir = 1-Dir; 
            }else if((ReverseStart.face[1][1].equals("W")||ReverseStart.face[1][1].equals("Y")) && store.isLR){
                Dir = 1-Dir; 
            }
            return movethree(Archive, ReverseStart, Origin, Dir, DirStore, RowCol);
        }
    }

    public static void copyFace(CubeFace dest, CubeFace src){
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                dest.face[i][j] = src.face[i][j];
            }
        }
    }

    public static void printFace(CubeFace src){
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                System.out.print(src.face[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void repeatTurns(int WhichWay,CubeFace src,int Dir, int RowCol){
        for(int i = 0; i<WhichWay; i++){
            movethree(null, src, src, Dir, Dir, RowCol);
        }
    }

    public static void main(String[] args){
        CubeFace FrontFace = new CubeFace("B");
        CubeFace BackFace = new CubeFace("G");
        CubeFace TopFace = new CubeFace("W");
        CubeFace BottomFace = new CubeFace("Y");
        CubeFace LeftFace = new CubeFace("R");
        LeftFace.isLR = true;
        CubeFace RightFace = new CubeFace("O");
        RightFace.isLR = true;

        LinkFace(LeftFace, TopFace, 0, 3,0); //1 is clockwise 90 itself
        LinkFace(TopFace, RightFace, 1, 0,1);
        LinkFace(RightFace, BottomFace, 0, 1,2);
        LinkFace(BottomFace, LeftFace, 1, 2,3);
        
        LinkFace(TopFace, FrontFace, 0, 3,3);
        LinkFace(BackFace, TopFace, 0, 1,3);
        LinkFace(BottomFace, BackFace, 0, 3,1);
        LinkFace(FrontFace, BottomFace, 0, 3,3);

        LinkFace(LeftFace, FrontFace, 1, 0, 0); //0:vertical, 
        LinkFace(FrontFace, RightFace, 1, 0, 0);
        LinkFace(RightFace, BackFace, 1,  0, 0);
        LinkFace(BackFace, LeftFace, 1,  0, 0);

        Scanner sc = new Scanner(System.in);
        String temp = sc.nextLine();
        while(!temp.equals("quit")){
            int rpt = temp.charAt(2)-'0';
            switch(temp.charAt(0)+""+temp.charAt(1)){
                case "FL":
                    repeatTurns(rpt,FrontFace, 0, 0);
                    break;
                case "FR":
                    repeatTurns(rpt,FrontFace, 0, 2);
                    break;
                case "FT":
                    repeatTurns(rpt, FrontFace, 1, 0);
                    break;
                case "FB":
                    repeatTurns(rpt, FrontFace, 1, 2);
                    break;
                case "TT":
                    repeatTurns(rpt, TopFace, 1, 0);
                    break;
                case "TB":
                    repeatTurns(rpt, TopFace, 1, 2);
                    break;
            }
            outputSituation(FrontFace,TopFace,BottomFace,BackFace,LeftFace,RightFace);
            temp = sc.nextLine();
        }
    
    }

    public static void outputSituation(CubeFace FrontFace,CubeFace TopFace,CubeFace BottomFace,CubeFace BackFace,CubeFace LeftFace,CubeFace RightFace){        
        System.out.println("top:");
        printFace(TopFace);
        System.out.println("front:");
        printFace(FrontFace);
        System.out.println("bottom:");
        printFace(BottomFace);
        System.out.println("back:");
        printFace(BackFace);
        System.out.println("left:");
        printFace(LeftFace);
        System.out.println("right:");
        printFace(RightFace);
        System.out.println("---------------------------");
    }
}

