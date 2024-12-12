import networkx as nx
import pydot
from networkx.drawing.nx_pydot import from_pydot, to_pydot,write_dot
import os
from datetime import datetime

funn = {}

def optimize_graph(G):
    remove_nodes = []
    for node, attr in G.nodes(data=True):
        if 'label' not in attr and G.in_degree(node) and G.out_degree(node):
            predecessors = list(G.predecessors(node))
            successors = list(G.successors(node))
            print("pred")
            print(predecessors)
            for pred in predecessors:
                for succ in successors:
                    if pred!=node and succ!=node and not G.has_edge(pred,succ):
                        G.add_edge(pred,succ)
            remove_nodes.append(node)
    for nd in remove_nodes:
        G.remove_node(nd)
      


def remove_dummy_nodes_and_connect_edges(dot_filename, output_filename):
    # Load the DOT file using pydot
    pydot_graph = pydot.graph_from_dot_file(dot_filename)[0]
    
    # Convert pydot graph to networkx graph
    G = from_pydot(pydot_graph)

    # Find all nodes with label "dummy_call"
    dummy_nodes = [n for n, attr in G.nodes(data=True) if attr.get('label') == '"dummy printf"']

    # For each dummy node, connect its predecessors to its successors and remove the dummy node
    for dummy_node in dummy_nodes:
        predecessors = list(G.predecessors(dummy_node))
        successors = list(G.successors(dummy_node))

        # Remove the dummy node from the graph
        G.remove_node(dummy_node)

        # Connect predecessors to successors
        for pred in predecessors:
            for succ in successors:
                G.add_edge(pred, succ)
          
    # optimize_graph(G)
    # Convert networkx graph back to pydot
    pydot_graph = to_pydot(G)

    # Save the modified graph to a DOT file
    pydot_graph.write_raw(output_filename)

def process_all_dot_files_in_folder(folder_path):
    print(folder_path)
    # Iterate through all .dot files in the folder
    for filename in os.listdir(folder_path):
        if filename.endswith(".dot"):
            input_dot_file = os.path.join(folder_path, filename)
            output_dot_file = os.path.join(folder_path, "modified_" + filename)
            
            print(f"Processing {input_dot_file}...")
            remove_dummy_nodes_and_connect_edges(input_dot_file, output_dot_file)
            print(f"Output saved to {output_dot_file}")


def Optii(graph,filename):
    merge_nodes = [n for n, attr in graph.nodes(data=True) if attr.get('label', '').startswith('"start_')]
    print("mere merge nodes :",merge_nodes)
    for node in merge_nodes:
        label = graph.nodes[node].get('label', 'No label')
        funname = label[7:-1]
        if funname not in funn:
            funn[funname] = 1
        else :
            funn[funname]+=1
        strr = "modified_"+funname+".dot"
        
        if strr == filename:
            deleted_nodes = []
            root_nodes = [node for node, in_degree in graph.in_degree() if in_degree == 0]
            successors = list(graph.successors(node))
            predecessors = list(graph.predecessors(node))
            deleted_nodes.append(node)
            for n in predecessors:
                graph.add_edge(n,root_nodes[0])
                current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
                node_id = f"root_{current_time}"
                label = "root"
                graph.add_node(node_id, label=label)
                graph.add_edge(node_id,root_nodes[0])
                for j in successors:
                    graph.add_edge(n,j)

            for i in deleted_nodes:
                graph.remove_node(i)
        
        else :
        
            fun_graph = pydot.graph_from_dot_file(strr)[0]
            g = from_pydot(fun_graph)
            node_mapping = {node_id: f"{funn[funname]}_{node_id}" for node_id in g.nodes()}
            G = nx.relabel_nodes(g, node_mapping)
            graph = nx.compose(graph,G)
            print(funn)
            print(f'Node: {node}, Label: {funname}')
            

            start = []
            end = []

            for nd in G.nodes():
                if G.in_degree(nd) == 0:
                    start.append(nd)        
                if G.out_degree(nd) == 0:  
                    end.append(nd)
            print(start)
            print(end)
            predecessors = list(graph.predecessors(node))
            successors = list(graph.successors(node))
            print(predecessors)
            for i in start:
                for j in predecessors:
                    print(i)
                    print(j)
                    graph.add_edge(j,i)
            for i in end:
                for j in successors:
                    print(i)
                    print(j)
                    graph.add_edge(i,j)
            graph.remove_node(node)
            optimize_graph(graph)
    return graph





# Example usage
folder_path = '/home/dev/Project'  # Replace with your folder path
process_all_dot_files_in_folder(folder_path)


for filename in os.listdir(folder_path):  
    if filename.startswith('modified_') and filename.endswith('.dot'):
        file_name = os.path.join(folder_path, filename)
        pydot_graph = pydot.graph_from_dot_file(filename)[0]
        g = from_pydot(pydot_graph)
        g = Optii(g,filename)        
        write_dot(g, filename)







pydot_graph = pydot.graph_from_dot_file("modified_main.dot")[0]
graph = from_pydot(pydot_graph)
graph = Optii(graph,"modified_main.dot")


# funn = {}
# merge_nodes = [n for n, attr in graph.nodes(data=True) if attr.get('label', '').startswith('"start_')]

# for node in merge_nodes:
#     label = graph.nodes[node].get('label', 'No label')
#     funname = label[7:-1]
#     if funname not in funn:
#         funn[funname] = 1
#     else :
#         funn[funname]+=1
#     strr = "modified_"+funname+".dot"
#     fun_graph = pydot.graph_from_dot_file(strr)[0]
#     g = from_pydot(fun_graph)
#     node_mapping = {node_id: f"{funn[funname]}.{node_id}" for node_id in g.nodes()}
#     G = nx.relabel_nodes(g, node_mapping)
#     graph = nx.compose(graph,G)
#     print(funn)
#     print(f'Node: {node}, Label: {funname}')
    

#     start = []
#     end = []

#     for nd in G.nodes():
#         if G.in_degree(nd) == 0:
#             start.append(nd)        
#         if G.out_degree(nd) == 0:
#             end.append(nd)
#     print(start)
#     print(end)
#     predecessors = list(graph.predecessors(node))
#     successors = list(graph.successors(node))
#     print(predecessors)
#     for i in start:
#         for j in predecessors:
#             print(i)
#             print(j)
#             graph.add_edge(j,i)
#     for i in end:
#         for j in successors:
#             print(i)
#             print(j)
#             graph.add_edge(i,j)
#     graph.remove_node(node)
    # optimize_graph(graph)

write_dot(graph, "ans.dot")
print(graph.nodes)
print(graph.edges)