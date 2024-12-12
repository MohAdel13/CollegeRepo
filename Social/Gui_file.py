import pandas as pd
import streamlit as st
import main


def page1():
    st.title("Import")
    options = ['Directed', 'Undirected']
    choice = st.radio('Graph Type:', options)
    st.write("Please select the CSV files:")
    Edges_file = st.file_uploader("Edges", type="csv", key="edges_uploader")
    if Edges_file is not None:
        edges = pd.read_csv(Edges_file)
        st.write('No. of edges:', len(edges))
        if st.button("Show Edges Dataset"):
            st.write(edges)
    Nodes_file = st.file_uploader("Nodes", type="csv", key="nodes_uploader")
    if Nodes_file is not None:
        nodes = pd.read_csv(Nodes_file)
        st.write('No. of nodes:', len(nodes))
        if st.button("Show Nodes Dataset"):
            st.write(nodes)
        if st.button("Start Analysis"):
            st.session_state.data = (nodes, edges)
            st.session_state.page = "page2"
            st.session_state.grType = choice


def page2():
    st.sidebar.title("")
    vis = st.sidebar.button('Visualize Graph', key='visual1')
    if vis:
        st.session_state.before = True
        st.session_state.previous = "page2"
        st.session_state.page = 'vis'
    back1 = st.sidebar.button('Back', key="Back To Page1")
    if back1:
        st.session_state.page = 'page1'
    nodes, edges = st.session_state.data
    G = main.create_graph(nodes, edges, st.session_state.grType)
    st.session_state.graph = G
    C = 0
    options = ["Girvan newmman", "Louvain", "Both and compare using NMI"]
    selected_option = st.selectbox("Select an community detection algorithm", options)
    if st.button("Start Detection"):
        if selected_option == "Girvan newmman":
            C = st.number_input("Enter A Community Count As A Stop Condition Or Enter Zero To Be Dynamic")
            st.session_state.detAlg = 'Girvan'
            st.session_state.page = "page3"

        elif selected_option == "Louvain":
            st.session_state.detAlg = 'Louvain'
            st.session_state.page = "page3"

        elif selected_option == "Both and compare using NMI":
            st.session_state.detAlg = 'Both'
            st.session_state.page = "page3"
        st.session_state.C = C


def page3():
    st.sidebar.title("")
    vis = st.sidebar.button('Visualize Graph', key='visual')
    if vis:
        st.session_state.before = True
        st.session_state.previous = "page3"
        st.session_state.page = 'vis'
    back2 = st.sidebar.button('Back', key="Back To Page2")
    if back2:
        st.session_state.page = 'page2'
    st.title('Community Detection')
    st.write('Please Wait While Detection Is Finished........')
    G = st.session_state.graph
    finished = False
    options = ["Girvan newmman", "Louvain"]
    selected = False
    C = st.session_state.C
    if st.session_state.detAlg == 'Louvain':
        part1 = list(main.com_detect(G, 'Louvain', C))
        finished = True

    elif st.session_state.detAlg == 'Girvan':
        part1 = list(main.com_detect(G, 'Girvan', C))
        finished = True

    else:
        part2 = list(main.com_detect(G, 'Louvain', C))
        part1 = list(main.com_detect(G, 'Girvan', C))
        nmi = main.NMI(G, part1, part2)
        finished = True

    if finished:
        if st.session_state.detAlg == 'Girvan':
            st.write('Girvan Results:')
            st.write('No. Of Communities:', len(set(part1[1].values())))
            st.write('Best Modularity Value:', part1[0])
            st.write('Modularity Of each Iteration:', part1[2])
            st.session_state.part = part1[1]
            selected = True

        elif st.session_state.detAlg == 'Louvain':
            st.write('Louvain Results:')
            st.write('No. Of Communities:', len(set(part1[1].values())))
            st.write('Best Modularity Value:', part1[0])
            st.session_state.part = part1[1]
            selected = True

        else:
            st.write('Girvan Results:')
            st.write('No. Of Communities:', len(set(part1[1].values())))
            st.write('Best Modularity Value:', part1[0])
            st.write('Modularity Of each Iteration:', part1[2])
            st.write('Louvain Results:')
            st.write('No. Of Communities:', len(set(part2[1].values())))
            st.write('Best Modularity Value:', part2[0])
            st.write('NMI Value:', nmi)
            selected_option = st.selectbox("Select an algorithm to continue with", options)
            if st.button('select'):
                st.session_state.detAlg = selected_option
                if selected_option == "Girvan newmman":
                    st.session_state.part = part1[1]
                else:
                    st.session_state.part = part2[1]
                selected = True
                st.session_state.page = "page4"
        if selected:
            if st.button('Statistics Operations'):
                st.session_state.page = "page4"


def page4():
    st.title('Statistics Operations')
    st.sidebar.title("")
    vis = st.sidebar.button('Visualize Graph', key='visual')
    if vis:
        st.session_state.before = False
        st.session_state.previous = "page4"
        st.session_state.page = 'vis'
    back3 = st.sidebar.button('Back', key="Back To Page3")
    if back3:
        st.session_state.page = 'page3'
    selected = False
    options = ["Community Detection Evaluations", "Nodes Filtering", "Link Analysis"]
    selected_option = st.selectbox("Choose Operation", options)
    if st.button('select'):
        st.session_state.statOper = selected_option
        st.session_state.page = "page5"


def page5():
    st.title(st.session_state.statOper)
    st.sidebar.title('')
    vis = st.sidebar.button('Visualize Graph', key='visual')
    if vis:
        st.session_state.before = False
        st.session_state.previous = "page5"
        st.session_state.page = 'vis'
    back4 = st.sidebar.button('Back', key='Back To Page4')
    if back4:
        st.session_state.page = 'page4'
    finished = False
    if st.session_state.statOper == "Link Analysis":
        pageRank = main.lnk_analsis(st.session_state.graph)
        st.write(pageRank)
    else:
        if st.session_state.statOper == "Community Detection Evaluations":
            options = ["Conductance", "Average Degree"]
            selected_option = st.selectbox("Choose Operation", options)
            if st.button('select'):
                evaluation = 0
                if selected_option == 'Conductance':
                    evaluation = main.conductance(st.session_state.graph, st.session_state.part)
                    finished = True
                elif selected_option == 'Average Degree':
                    evaluation = main.avg_deg(st.session_state.graph, st.session_state.part)
                    finished = True
                if finished:
                    st.write(evaluation)
        else:
            threshold = st.number_input('Enter A Min Value')
            finished = False
            options = ["Degree", "Betweenness", "Eigenvector"]
            selected_option = st.selectbox("Choose Centrality Measure", options)
            if st.button('Select'):
                if selected_option == "Degree":
                    value = main.nodes_filter(st.session_state.graph, threshold, 'degree')
                elif selected_option == "Betweenness":
                    value = main.nodes_filter(st.session_state.graph, threshold, 'betweenness')
                else:
                    value = main.nodes_filter(st.session_state.graph, threshold, 'eigenvector')
                finished = True
                if finished:
                    st.write(value)


def visualPage(before):
    st.title("Graph")
    st.sidebar.title('')
    back5 = st.sidebar.button('Back', key='Back')
    if back5:
        st.session_state.page = st.session_state.previous
    selected = False
    if st.session_state.fig is None:
        fig1 = main.visualize(st.session_state.graph, 0, 0, None)
    else:
        fig1 = st.session_state.fig
    st.plotly_chart(fig1)
    if not before:
        options = ["Size By Degree", "Color By Community", "Both"]
    else:
        options = ["Size By Degree"]
    selected_option = st.selectbox("Adjust Nodes...", options)
    if st.button('select'):
        if selected_option == 'Size By Degree':
            fig = main.visualize(st.session_state.graph, 0, 1, None)
            selected = True
        elif selected_option == 'Color By Community':
            fig = main.visualize(st.session_state.graph, 1, 0, st.session_state.part)
            selected = True
        elif selected_option == 'Both':
            fig = main.visualize(st.session_state.graph, 1, 1, st.session_state.part)
            selected = True

    if selected:
        st.plotly_chart(fig)


def app():
    if "page" not in st.session_state:
        st.session_state.page = "page1"
        st.session_state.data = None
        st.session_state.detAlg = None
        st.session_state.graph = None
        st.session_state.statOper = None
        st.session_state.part = None
        st.session_state.C = None
        st.session_state.fig = None
        st.session_state.grType = None
        st.session_state.before = None
        st.session_state.previous = None

    if st.session_state.page == "page1":
        page1()
    elif st.session_state.page == "page2":
        page2()
    elif st.session_state.page == "page3":
        page3()
    elif st.session_state.page == "page4":
        page4()
    elif st.session_state.page == "page5":
        page5()
    elif st.session_state.page == "vis":
        if st.session_state.before:
            visualPage(True)
        else:
            visualPage(False)


if __name__ == '__main__':
    app()
