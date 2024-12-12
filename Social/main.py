import networkx as nx
import community
from matplotlib import cm
from sklearn.metrics import normalized_mutual_info_score
import plotly.graph_objects as go
import matplotlib.colors as colors
import numpy as np


def create_graph(nodes, edges, typ):
    if typ == 'Directed':
        G = nx.DiGraph()
    else:
        G = nx.Graph()
    for i, row in nodes.iterrows():
        G.add_node(str(row['ID']), attr_dict=row.to_dict())
    for i, row in edges.iterrows():
        G.add_edge(str(row['Source']), str(row['Target']))
    if G.is_directed():
        G = G.to_undirected()
    return G


def com_detect(gr, typ, C):
    part = None
    best_modularity = -1
    if typ == 'Girvan':
        part, best_modularity, modularities = girvan_newman(gr, C)
        return best_modularity, part, modularities

    elif typ == 'Louvain':
        part = community.best_partition(gr)
        best_modularity = community.modularity(part, gr)
    return best_modularity, part


def conductance(G, part):
    id = 0
    for community_id in set(part.values()):
        nodes = [n for n in part.keys() if part[n] == community_id]
        cond = nx.algorithms.cuts.conductance(G, nodes)
        if id == 0:
            min_conductance = cond
            id += 1
        elif cond < min_conductance:
            min_conductance = cond
    return min_conductance


def avg_deg(G, part):
    degrees = []
    for node in part.keys():
        degree = G.degree(node)
        degrees.append(degree)

    avg_degree = np.sum(degrees) / len(G.nodes())
    return avg_degree


def NMI(gr, part1, part2):

    labels1 = [part1[1][node] for node in gr.nodes()]
    labels2 = [part2[1][node] for node in gr.nodes()]

    nmi = normalized_mutual_info_score(labels1, labels2)
    return nmi


def lnk_analsis(gr):
    pgrnk = nx.pagerank(gr)
    return pgrnk


def nodes_filter(gr, threshold, typ):

    filtered_nodes = []

    if typ == 'degree':
        degree_centrality = nx.degree(gr)
        filtered_nodes = [n for n in gr.nodes() if degree_centrality[n] >= threshold]

    elif typ == 'betweenness':
        betweenness_centrality = nx.betweenness_centrality(gr)
        filtered_nodes = [n for n in gr.nodes() if betweenness_centrality[n] >= threshold]

    elif typ == 'eigenvector':
        eigenvector_centrality = nx.eigenvector_centrality(gr)
        filtered_nodes = [n for n in gr.nodes() if eigenvector_centrality[n] >= threshold]

    return filtered_nodes


def visualize(gr, node_cls, node_sizes, part):
    sizes = []
    color_names = []
    color_map = []
    pos = nx.spring_layout(gr)
    node_x = [pos[k][0] for k in gr.nodes()]
    node_y = [pos[k][1] for k in gr.nodes()]
    if node_cls == 0:
        for i in gr.nodes():
            color_names.append('cyan')
    else:
        num_communities = len(set(part.values()))
        cls = cm.tab20(range(num_communities))
        for node in gr.nodes():
            color_map.append(cls[part[node]])
        color_names = [colors.to_hex(arr) for arr in color_map]
    if node_sizes == 0:
        for i in gr.nodes():
            sizes.append(20)
    else:
        for node in gr.nodes:
            degree = gr.degree[node]
            size = 20 + (3 * degree)
            sizes.append(size)
    node_trace = go.Scatter(
        x=node_x, y=node_y,
        mode='markers',
        marker=dict(
            size=sizes,
            color=color_names,
            line_width=0,
            opacity=0.8),
        text=list(gr.nodes()))

    edge_x = []
    edge_y = []
    for e in gr.edges():
        x0, y0 = pos[e[0]]
        x1, y1 = pos[e[1]]
        edge_x.extend([x0, x1, None])
        edge_y.extend([y0, y1, None])

    edge_trace = go.Scatter(
        x=edge_x, y=edge_y,
        line=dict(width=0.5, color='#289'),
        hoverinfo='none',
        mode='lines')

    fig = go.Figure(data=[edge_trace, node_trace],
                    layout=go.Layout(
                        title='Interactive Graph Visualization',
                        titlefont_size=16,
                        showlegend=False,
                        hovermode='closest',
                        margin=dict(b=20, l=5, r=5, t=40),
                        annotations=[dict(
                            text="",
                            showarrow=False,
                            xref="paper", yref="paper",
                            x=0.005, y=-0.002)],
                        xaxis=dict(showgrid=False, zeroline=False, showticklabels=False),
                        yaxis=dict(showgrid=False, zeroline=False, showticklabels=False)))

    fig.update_layout(dragmode='pan')
    return fig


def girvan_newman(gr, C):
    G = gr.copy()
    best_modularity = -1
    best_communities = None
    modularities = []
    while True:
        betweenness = nx.edge_betweenness_centrality(G)
        max_betweenness = max(betweenness.values())
        edges_to_remove = [e for e, centrality in betweenness.items() if centrality == max_betweenness]
        G.remove_edges_from(edges_to_remove)
        communities = list(nx.connected_components(G))
        modularity = nx.algorithms.community.modularity(gr, communities)
        modularities.append(modularity)

        if modularity > best_modularity:
            best_modularity = modularity
            best_communities = communities
        if C == 0 and G.number_of_edges() == 0:
            break
        if (C <= len(communities)) and (C != 0):
            break
    community_dict = {}
    for i, com in enumerate(best_communities):
        for node in com:
            community_dict[node] = i
    return community_dict, best_modularity, modularities
