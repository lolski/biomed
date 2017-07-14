import grakn.client as gc

def compute_degrees(q):
    graph = gc.Graph("http://localhost:4567", "biomed")
    result = graph.execute(q)
    return result

def persist_degrees(deg_dic):
    graph = gc.Graph("http://localhost:4567", "biomed")
    for deg in deg_dic:
        q = "match $x id '{}'; insert $x has degree {};"
        for n_id in deg_dic[deg]:
            query = q.format(n_id, deg)
            graph.execute(query)

print ("Computing degrees...")
deg1 = compute_degrees("compute degrees of interaction in interaction, reference;")
print ("Computing more degrees...")
deg2 = compute_degrees("compute degrees of gene-target in gene-target, reference;")
print ("Finished Computing degrees.")
print("Persisting degrees...")
persist_degrees(deg1)
print("Persisting more degrees...")
persist_degrees(deg2)
print("Finished persisting degrees.")
