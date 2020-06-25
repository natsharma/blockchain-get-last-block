import hashlib
#returns the last block on the chain
def lastBlock(startBalances, pendingTransactions, blockSize):
    currentHash = '0000000000000000000000000000000000000000'

    while (pendingTransactions):
        currentTransactions = validTransactions(startBalances, pendingTransactions, blockSize)
        if(not currentTransactions):
            break
        previousHash = currentHash
        nonce = searchForNonce(previousHash, currentTransactions)
        currentHash = sha1(previousHash + ', ' + str(nonce) + ', ' + str(currentTransactions))
        lastBlock=currentHash + ', ' + previousHash + ', ' + str(nonce) + ', ' + str(currentTransactions)

    return lastBlock

def sha1(text):
    s = hashlib.sha1()
    s.update(text.encode('utf-8'))
    return s.hexdigest()

def searchForNonce(prevBlockHash, transactions):
    found = 0
    nonce = 1
    while found == 0:
        hash = sha1(prevBlockHash + ', ' + str(nonce) + ', ' + str(transactions))
        if hash[0:4] == '0000':
            found = 1
            #print("curren hash " + hash)
            break
        #print(hash)
        #print(nonce)
        nonce += 1
    return nonce

def validTransactions(startBalances, pendingTransactions, blockSize):
    approvedTransactions = []
    count = 0
    while (count < blockSize) and pendingTransactions:
        if startBalances[pendingTransactions[0][0]] >= pendingTransactions[0][2]:
            approvedTransactions.append(pendingTransactions[0])
            updateBalance(startBalances, pendingTransactions[0])
        del pendingTransactions[0]
        count += 1
    return approvedTransactions

def updateBalance(startingBalance, currentTransactions):
    startingBalance[currentTransactions[1]] = startingBalance[currentTransactions[1]] + currentTransactions[2]
    startingBalance[currentTransactions[0]] = startingBalance[currentTransactions[0]] - currentTransactions[2]

def main():
    print("LAST BLOCK" + lastBlock([3, 10, 10, 3], [[3,2,2], [2,3,5], [3,2,4], [3,0,2], [1,2,2]], 2))
    #print("LAST BLOCK" + lastBlock([5, 0, 0], [[0, 1, 5], [1, 2, 5]], 2))
    #print("LAST BLOCK" + lastBlock([1,7], [[1, 0, 4], [1, 0, 3], [1, 0, 2]], 2))


if __name__ == '__main__':
    main()
