#!/usr/bin/python

from jinja2 import Environment, FileSystemLoader
import os
components = {'component_name': 'api', 'repo_name': 'massbitroute', 'DEFAULT_COMPONENT_TAG_IN_BASE': 'export api=$(cat $RUNTIME_DIR/${network_number}/tags/API)', 'PR_COMPONENT_TAG_IN_BASE': 'export api=${PR_NUMBER}', 'jenkins_name': 'MassbitAPI'}
vars = {
    'TEST_BRANCH': 'feature/jenkins-template'
}
setup_stages = [
    {'name': 'Create docker network',       'steps': ['bash ./create_docker_network.sh'] },
    {'name': 'Get latest git tag',          'steps': ['bash ./check_latest_tag.sh _read_latest_git_tags'] },
    {'name': 'Prepare runtime environment', 'steps': ['bash -x prepare_runtime.sh'] },
    {'name': 'Prepare proxy',               'steps': ['bash -x prepare_proxy.sh'] },
    {'name': 'Create git docker',           'steps': ['bash -x create_git.sh'] },
    {'name': 'Start docker compose',        'steps': ['bash -x create_docker_compose.sh'] },
    {'name': 'Start stat docker compose',   'steps': ['bash -x create_stat_docker_compose.sh'] },
    {'name': 'Run API test',                'steps': ['bash -x run_component_api_test.sh'] },
]

scenarios = os.listdir('../end2end/scenarios-enable/')

file_loader = FileSystemLoader('templates')
env = Environment(loader=file_loader)

template = env.get_template('./jenkins.pipeline.template')

output = template.render(comps=components, stages=setup_stages, scenarios=scenarios, vars=vars )
print(output)
