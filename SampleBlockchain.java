import java.util.*;

public class SampleBlockchain {
	
	public static void main(String[] args) {
		
		/*
		 * sample inputs for test
		 * start balances, pending transactions, blocksize
		 * {5, 0, 0}, {{0, 1, 5}, {1, 2, 5}}, 2
		 * {2, 8, 7, 3, 3, 9, 7}, {{1, 4, 4}, {5, 4, 5}, {6, 3, 5}, {1, 4, 1}, {5, 3, 4}, {6, 3, 1}, {1, 4, 3}}, 3
		 * {3, 10, 10, 3}, {{3, 2, 2}, {2, 3, 5}, {3, 2, 4}, {3, 0, 2}, {1, 2, 2}, 2
		 * {1, 7}, {{1, 0, 4}, {1, 0, 3}, {1, 0, 2}, 2
		 * 
		 */
		int[] startBalances = {5, 0, 0};
		int[][] pendingTransactions = {{0, 1, 5}, {1, 2, 5}};
		int blockSize = 2;
		System.out.println(lastBlock(startBalances, pendingTransactions, blockSize));
		
	}
	
	static String lastBlock(int[] startBalances, int[][] pendingTransactions, int blockSize) {
		String currentHash = "0000000000000000000000000";
		int i =0;
		while(i < pendingTransactions.length) {
			String currentTransactions = validTransactions(startBalances, pendingTransactions, blockSize);
			String prevBlockHash = currentHash;
			int nonce = searchForNonce(prevBlockHash, currentTransactions);
			currentHash = sha1(prevBlockHash + ", " + String.valueOf(nonce) + ", " + currentTransactions);
			String latestBlock = currentHash + ", " + prevBlockHash + ", " + String.valueOf(nonce)+ ", " + currentTransactions;
		}
		return "";
	}
	
	static String sha1(String text) {
		String sha1 = "";
		try {
			java.security.MessageDigest crypt = java.security.MessageDigest.getInstance("SHA-1");
			crypt.update(text.getBytes("UTF-8"));
			Formatter formatter = new Formatter();
			for(byte b : crypt.digest()) {
				formatter.format("%02x", b);
			}
			sha1 = formatter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sha1;
	}
	
	static int searchForNonce(String prevHash, String transactions) {
		int found = 0;
		int nonce = 1;
		while(found == 0) {
			String curr = sha1(String.valueOf(nonce) + prevHash);
			if(curr.substring(0, 4) == "0000") {
				found = 1;
				break;
			}
			nonce++;
		}
		return nonce;
	}
	
	static String validTransactions(int[] startBalances, int[][] pendingTransactions, int blockSize) {
		ArrayList<int[]> approvedTransactions = new ArrayList<int[]>();	
		int count = 0;
		int i = 0;
		while(count < blockSize && i < pendingTransactions.length) {
			if(startBalances[pendingTransactions[0][0]] >= pendingTransactions[0][2]) {
				approvedTransactions.add(pendingTransactions[0]);
				updateBalance(startBalances, pendingTransactions[0]);
			}
			i++;
			count++;
		}
		return approvedTransactions.toString();
	}
	
	static void updateBalance(int[] startBalances, int[] currentTransactions) {
		startBalances[currentTransactions[1]] = startBalances[currentTransactions[1]] + currentTransactions[2];
		startBalances[currentTransactions[0]] = startBalances[currentTransactions[0]] - currentTransactions[2];
	}
	
	

}
